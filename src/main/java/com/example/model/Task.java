package com.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private boolean completed;
    private String createdAt;

    // Конструктор
    public Task() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    // Конструктор с параметрами
    public Task(int id, String title) {
        this();
        this.id = id;
        this.title = title;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getStatusText() {
        return completed ? "✓ Выполнено" : "⏳ Ожидает";
    }

    public String getStatusClass() {
        return completed ? "completed" : "pending";
    }

}
