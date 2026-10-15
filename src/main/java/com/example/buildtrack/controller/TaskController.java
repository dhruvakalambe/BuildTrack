
package com.example.buildtrack.controller;

import com.example.buildtrack.entity.Project;
import com.example.buildtrack.entity.Task;
import com.example.buildtrack.service.ProjectService;
import com.example.buildtrack.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService,
                          ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    @GetMapping("/new")
    public String newTask(
            @RequestParam(required = false) Long projectId,
            Model model) {

        Task task = new Task();
        task.setProject(projectId == null
                ? new Project()
                : projectService.getProjectById(projectId));

        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Create Task");

        return "task-form";
    }



    @PostMapping
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            @RequestParam(value = "projectId", required = false) Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.rejectValue("project", "required",
                    "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Create Task");
            return "task-form";
        }

        task.setProject(projectService.getProjectById(projectId));
        taskService.saveTask(task);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Task created successfully.");

        return "redirect:/tasks";
    }



    @GetMapping("/{id}/edit")
    public String editTask(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Edit Task");
        return "task-form";
    }


    @PostMapping("/{id}/edit")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            @RequestParam(value = "projectId", required = false) Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.rejectValue("project", "required",
                    "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Edit Task");
            return "task-form";
        }

        Task existing = taskService.getTaskById(id);

        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setAssignee(task.getAssignee());
        existing.setPriority(task.getPriority());
        existing.setStatus(task.getStatus());
        existing.setDeadline(task.getDeadline());
        existing.setProject(projectService.getProjectById(projectId));

        taskService.saveTask(existing);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Task updated successfully.");

        return "redirect:/tasks";
    }


    @PostMapping("/{id}/delete")
    public String deleteTask(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        taskService.deleteTask(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Task deleted successfully.");

        return "redirect:/tasks";
    }
}
