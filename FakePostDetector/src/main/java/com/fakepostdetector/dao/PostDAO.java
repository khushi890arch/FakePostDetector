package com.fakepostdetector.dao;

import com.fakepostdetector.config.DatabaseConfig;
import com.fakepostdetector.model.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDAO {
    private final DatabaseConfig dbConfig;

    public PostDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public Post save(Post post) throws SQLException {
        String sql = "INSERT INTO posts (user_id, content, title, credibility_score, is_fake) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, post.getUserId());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getTitle());
            pstmt.setDouble(4, post.getCredibilityScore());
            pstmt.setBoolean(5, post.getIsFake());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating post failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setPostId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        }
        return post;
    }

    public Optional<Post> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPost(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Post> findAll() throws SQLException {
        String sql = "SELECT * FROM posts";
        List<Post> posts = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                posts.add(mapResultSetToPost(rs));
            }
        }
        return posts;
    }

    public void update(Post post) throws SQLException {
        String sql = "UPDATE posts SET content = ?, title = ?, credibility_score = ?, is_fake = ? WHERE post_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, post.getContent());
            pstmt.setString(2, post.getTitle());
            pstmt.setDouble(3, post.getCredibilityScore());
            pstmt.setBoolean(4, post.getIsFake());
            pstmt.setLong(5, post.getPostId());
            
            pstmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM posts WHERE post_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        return Post.builder()
                .postId(rs.getLong("post_id"))
                .userId(rs.getLong("user_id"))
                .content(rs.getString("content"))
                .title(rs.getString("title"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .credibilityScore(rs.getDouble("credibility_score"))
                .isFake(rs.getBoolean("is_fake"))
                .build();
    }
} 