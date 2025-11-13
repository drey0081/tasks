<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Task" %>
<%@ page import="com.example.repository.TaskRepository" %>
<%@ page import="java.util.List" %>
<%
    // Получаем данные напрямую из request
    TaskRepository repository = TaskRepository.getInstance();
    List<Task> tasks = repository.getAllTasks();
    int totalTasks = repository.getTotalCount();
    int completedTasks = repository.getCompletedCount();
    int pendingTasks = totalTasks - completedTasks;

    // Получаем сообщения из сессии
    String message = (String) session.getAttribute("message");
    String error = (String) session.getAttribute("error");

    // Удаляем сообщения после показа
    if (message != null) session.removeAttribute("message");
    if (error != null) session.removeAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>To-Do List (Pure Java)</title>
    <style>
        <%@ include file="/WEB-INF/css/style.css" %>
    </style>
</head>
<body>
<div class="container">
    <header>
        <h1>📝 To-Do List на чистой Java</h1>
        <p>Servlets + JSP - Без JavaScript и JSTL!</p>
    </header>

    <!-- Сообщения -->
    <% if (message != null) { %>
    <div class="message success">
        <%= message %>
    </div>
    <% } %>

    <% if (error != null) { %>
    <div class="message error">
        <%= error %>
    </div>
    <% } %>

    <!-- Форма добавления задачи -->
    <div class="add-task-form">
        <form action="<%= request.getContextPath() %>/tasks/action" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <input type="text" name="title" placeholder="Введите новую задачу..."
                       maxlength="100" required class="text-input">
                <button type="submit" class="btn btn-primary">Добавить задачу</button>
            </div>
        </form>
    </div>

    <!-- Статистика -->
    <div class="stats">
        <div class="stat-card">
            <h3><%= totalTasks %></h3>
            <p>Всего задач</p>
        </div>
        <div class="stat-card">
            <h3><%= pendingTasks %></h3>
            <p>Ожидает</p>
        </div>
        <div class="stat-card">
            <h3><%= completedTasks %></h3>
            <p>Выполнено</p>
        </div>
    </div>

    <!-- Действия с задачами -->
    <div class="bulk-actions">
        <form action="<%= request.getContextPath() %>/tasks/action" method="post"
              style="display: inline;">
            <input type="hidden" name="action" value="toggleAll">
            <button type="submit" class="btn btn-secondary">Переключить все</button>
        </form>

        <form action="<%= request.getContextPath() %>/tasks/action" method="post"
              style="display: inline;">
            <input type="hidden" name="action" value="deleteCompleted">
            <button type="submit" class="btn btn-danger">Удалить выполненные</button>
        </form>
    </div>

    <!-- Список задач -->
    <% if (tasks.isEmpty()) { %>
    <div class="empty-state">
        <h2>🎉 Нет задач!</h2>
        <p>Добавьте первую задачу используя форму выше</p>
    </div>
    <% } else { %>
    <div class="tasks-list">
        <% for (Task task : tasks) { %>
        <div class="task-item <%= task.isCompleted() ? "completed" : "pending" %>">
            <div class="task-info">
                <span class="task-id">#<%= task.getId() %></span>
                <span class="task-title"><%= escapeHtml(task.getTitle()) %></span>
                <span class="task-date"><%= task.getCreatedAt() %></span>
            </div>

            <div class="task-status">
                            <span class="status-badge">
                                <%= task.isCompleted() ? "✓ Выполнено" : "⏳ Ожидает" %>
                            </span>
            </div>

            <div class="task-actions">
                <form action="<%= request.getContextPath() %>/tasks/action"
                      method="post" style="display: inline;">
                    <input type="hidden" name="action" value="toggle">
                    <input type="hidden" name="taskId" value="<%= task.getId() %>">
                    <button type="submit" class="btn btn-sm <%= task.isCompleted() ? "btn-warning" : "btn-success" %>">
                        <%= task.isCompleted() ? "❌ Отменить" : "✅ Выполнить" %>
                    </button>
                </form>

                <form action="<%= request.getContextPath() %>/tasks/action"
                      method="post" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="taskId" value="<%= task.getId() %>">
                    <button type="submit" class="btn btn-sm btn-danger"
                            onclick="return confirm('Удалить задачу \"<%= escapeJavaScript(task.getTitle()) %>\"?')">
                    🗑️ Удалить
                    </button>
                </form>
            </div>
        </div>
        <% } %>
    </div>
    <% } %>

    <!-- Информация о технологии -->
    <footer class="tech-info">
        <hr>
        <p><strong>Технологии:</strong> Java Servlets, JSP, HTML, CSS</p>
        <p><strong>Особенность:</strong> Без JavaScript, без JSTL, без интернета</p>
    </footer>
</div>
</body>
</html>

<%!
    // Вспомогательные методы для экранирования HTML и JavaScript
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String escapeJavaScript(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
%>