
package com.example.buildtrack.service;

import com.example.buildtrack.entity.Task;
import com.example.buildtrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Task not found: " + id));
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public long getOverdueTaskCount() {
        return taskRepository.findAll().stream()
                .filter(task -> task.getDeadline() != null)
                .filter(task -> task.getDeadline().isBefore(LocalDate.now()))
                .filter(task -> !"Completed".equals(task.getStatus()))
                .count();
    }

    public long getCompletedTaskCount() {
        return taskRepository.countByStatus("Completed");
    }


    public int getProjectProgress(Long projectId) {
        long totalTasks = taskRepository.countByProjectId(projectId);

        if (totalTasks == 0) {
            return 0;
        }

        long completedTasks =
                taskRepository.countByProjectIdAndStatus(projectId, "Completed");

        return (int) Math.round(
                (completedTasks * 100.0) / totalTasks
        );
    }


    public long getOverdueTaskCountByProject(Long projectId) {
        java.time.LocalDate today = java.time.LocalDate.now();

        return taskRepository.findByProjectId(projectId).stream()
                .filter(task -> task.getDeadline() != null)
                .filter(task -> task.getDeadline().isBefore(today))
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .count();
    }

}
