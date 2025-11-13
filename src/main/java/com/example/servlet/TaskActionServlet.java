package com.example.servlet;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/tasks/action")
public class TaskActionServlet extends HttpServlet {
    private TaskRepository repository;

    public void init() throws ServletException {
        this.repository = TaskRepository.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String taskIdParam = request.getParameter("taskId");

        try {
            switch (action) {
                case "add":
                    handleAddTask(request);
                    break;

                case "toggle":
                    if (taskIdParam != null) {
                        int taskId = Integer.parseInt(taskIdParam);
                        repository.toggleTask(taskId);
                    }
                    break;

                case "delete":
                    if (taskIdParam != null) {
                        int taskId = Integer.parseInt(taskIdParam);
                        repository.deleteTask(taskId);
                    }
                    break;

                case "deleteCompleted":
                    repository.deleteCompletedTasks();
                    break;

                case "toggleAll":
                    toggleAllTasks();
                    break;
            }

            // Добавляем сообщение об успехе
            request.getSession().setAttribute("message", getSuccessMessage(action));

        } catch (Exception e) {
            // Добавляем сообщение об ошибке
            request.getSession().setAttribute("error", "Ошибка: " + e.getMessage());
        }

        // Перенаправляем обратно на страницу задач
        response.sendRedirect(request.getContextPath() + "/tasks");
    }

    private void handleAddTask(HttpServletRequest request) {
        String title = request.getParameter("title");
        if (title != null && !title.trim().isEmpty()) {
            Task task = new Task();
            task.setTitle(title.trim());
            repository.addTask(task);
        } else {
            throw new IllegalArgumentException("Название задачи не может быть пустым");
        }
    }

    private void toggleAllTasks() {
        boolean allCompleted = repository.getAllTasks().stream().allMatch(Task::isCompleted);
        repository.getAllTasks().forEach(task -> {
            task.setCompleted(!allCompleted);
            repository.updateTask(task);
        });
    }

    private String getSuccessMessage(String action) {
        return switch (action) {
            case "add" -> "Задача успешно добавлена!";
            case "toggle" -> "Статус задачи изменен!";
            case "delete" -> "Задача удалена!";
            case "deleteCompleted" -> "Выполненные задачи удалены!";
            case "toggleAll" -> "Статус всех задач изменен!";
            default -> "Действие выполнено успешно!";
        };
    }
}