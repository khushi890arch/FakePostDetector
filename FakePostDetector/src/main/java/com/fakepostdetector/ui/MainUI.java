package com.fakepostdetector.ui;

import com.fakepostdetector.dao.PostDAO;
import com.fakepostdetector.model.Post;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MainUI extends Application {
    private final PostDAO postDAO = new PostDAO();
    private TextArea contentArea;
    private TextField titleField;
    private TableView<Post> postsTable;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fake Post Detector");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
        mainLayout.getStyleClass().add("main-layout");

        // Create input section
        VBox inputSection = createInputSection();
        mainLayout.setTop(inputSection);

        // Create posts table
        createPostsTable();
        mainLayout.setCenter(postsTable);

        // Create action buttons
        HBox actionButtons = createActionButtons();
        mainLayout.setBottom(actionButtons);

        // Apply CSS styling
        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load initial data
        refreshPostsTable();
    }

    private VBox createInputSection() {
        VBox inputSection = new VBox(10);
        inputSection.setPadding(new Insets(10));
        inputSection.getStyleClass().add("input-section");

        // Title input
        Label titleLabel = new Label("Post Title:");
        titleField = new TextField();
        titleField.getStyleClass().add("text-input");

        // Content input
        Label contentLabel = new Label("Post Content:");
        contentArea = new TextArea();
        contentArea.setPrefRowCount(3);
        contentArea.getStyleClass().add("text-input");

        inputSection.getChildren().addAll(titleLabel, titleField, contentLabel, contentArea);
        return inputSection;
    }

    private void createPostsTable() {
        postsTable = new TableView<>();
        postsTable.getStyleClass().add("posts-table");

        TableColumn<Post, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        TableColumn<Post, Double> scoreCol = new TableColumn<>("Credibility Score");
        scoreCol.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getCredibilityScore()).asObject());

        TableColumn<Post, Boolean> isFakeCol = new TableColumn<>("Is Fake");
        isFakeCol.setCellValueFactory(cellData -> 
            new SimpleBooleanProperty(cellData.getValue().getIsFake()));

        postsTable.getColumns().addAll(titleCol, scoreCol, isFakeCol);
    }

    private HBox createActionButtons() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getStyleClass().add("button-box");

        Button analyzeButton = new Button("Analyze Post");
        analyzeButton.getStyleClass().add("primary-button");
        analyzeButton.setOnAction(e -> analyzePost());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("secondary-button");
        clearButton.setOnAction(e -> clearInputs());

        buttonBox.getChildren().addAll(analyzeButton, clearButton);
        return buttonBox;
    }

    private void analyzePost() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Error", "Please fill in both title and content fields.");
            return;
        }

        // Create a new post (using dummy user ID for now)
        Post post = Post.builder()
                .userId(1L)
                .title(title)
                .content(content)
                .credibilityScore(calculateCredibilityScore(content))
                .isFake(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        try {
            postDAO.save(post);
            refreshPostsTable();
            clearInputs();
            showAlert("Success", "Post analyzed and saved successfully!");
        } catch (SQLException ex) {
            showAlert("Error", "Failed to save post: " + ex.getMessage());
        }
    }

    private double calculateCredibilityScore(String content) {
        // TODO: Implement actual credibility analysis
        // This is a dummy implementation
        return Math.random() * 100;
    }

    private void clearInputs() {
        titleField.clear();
        contentArea.clear();
    }

    private void refreshPostsTable() {
        try {
            postsTable.getItems().clear();
            postsTable.getItems().addAll(postDAO.findAll());
        } catch (SQLException ex) {
            showAlert("Error", "Failed to load posts: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 