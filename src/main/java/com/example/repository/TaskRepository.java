package com.example.repository;

import com.example.model.Task;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRepository {
    private static TaskRepository instance;
    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    // Конструктор
    private TaskRepository() {
        // Добавим тестовые данные
        addTask(new Task(1, "Изучить Java Servlets"));
        addTask(new Task(2, "Создать первое веб-приложение"));
        addTask(new Task(3, "Разобраться с JSP"));
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>(tasks.values());
        taskList.sort(Comparator.comparingInt(Task::getId));
        return taskList;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        int id = nextId.getAndIncrement();
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void toggleTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setCompleted(!task.isCompleted());
        }
    }

    public void deleteCompletedTasks() {
        tasks.values().removeIf(Task::isCompleted);
    }

    public int getTotalCount() {
        return tasks.size();
    }

    public int getCompletedCount() {
        return (int) tasks.values().stream().filter(Task::isCompleted).count();
    }

}
