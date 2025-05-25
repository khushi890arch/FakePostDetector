-- Create the database
CREATE DATABASE IF NOT EXISTS fake_post_detector;
USE fake_post_detector;

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Posts table
CREATE TABLE IF NOT EXISTS posts (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    credibility_score FLOAT DEFAULT 0.0,
    is_fake BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create Analysis_Results table
CREATE TABLE IF NOT EXISTS analysis_results (
    result_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    analysis_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sentiment_score FLOAT,
    fact_check_score FLOAT,
    source_reliability_score FLOAT,
    overall_credibility_score FLOAT,
    analysis_details TEXT,
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

-- Create Sources table
CREATE TABLE IF NOT EXISTS sources (
    source_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    url VARCHAR(255),
    reliability_score FLOAT DEFAULT 0.0,
    last_checked TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
); 