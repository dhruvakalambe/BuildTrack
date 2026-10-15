
package com.example.buildtrack.controller;

import com.example.buildtrack.entity.Project;
import com.example.buildtrack.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects";
    }

    @GetMapping("/new")
    public String newProject(Model model) {
        Project project = new Project();
        project.setStatus("Planned");
        model.addAttribute("project", project);
        model.addAttribute("pageTitle", "Create Project");
        return "project-form";
    }

    @PostMapping
    public String createProject(
            @Valid @ModelAttribute("project") Project project,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        validateDates(project, result);

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Create Project");
            return "project-form";
        }

        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Project created successfully!");

        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String projectDetails(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("project", projectService.getProjectById(id));
            return "project-details";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Project not found.");
            return "redirect:/projects";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProject(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("project", projectService.getProjectById(id));
            model.addAttribute("pageTitle", "Edit Project");
            return "project-form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Project not found.");
            return "redirect:/projects";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateProject(
            @PathVariable Long id,
            @Valid @ModelAttribute("project") Project project,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!projectService.projectExists(id)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Project not found.");
            return "redirect:/projects";
        }

        validateDates(project, result);

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Project");
            return "project-form";
        }

        projectService.updateProject(id, project);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Project updated successfully!");

        return "redirect:/projects/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Project deleted successfully!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Could not delete this project. Remove its associated records first.");
        }

        return "redirect:/projects";
    }

    private void validateDates(Project project, BindingResult result) {
        if (project.getStartDate() != null
                && project.getDeadline() != null
                && project.getDeadline().isBefore(project.getStartDate())) {
            result.rejectValue(
                    "deadline", "date.invalid",
                    "Deadline cannot be before the start date.");
        }
    }
}
