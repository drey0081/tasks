package com.example.servlet;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private TaskRepository repository;

    public void init() throws ServletException {
        this.repository = TaskRepository.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Устанавливаем атрибуты для JSP
        request.setAttribute("tasks", repository.getAllTasks());
        request.setAttribute("totalTasks", repository.getTotalCount());
        request.setAttribute("completedTasks", repository.getCompletedCount());
        request.setAttribute("pendingTasks", repository.getTotalCount() - repository.getCompletedCount());

        // Передаем управление JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tasks.jsp");
        dispatcher.forward(request, response);
    }
}
