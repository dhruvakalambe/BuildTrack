
package com.example.buildtrack.controller;

import com.example.buildtrack.entity.Project;
import com.example.buildtrack.entity.SiteIssue;
import com.example.buildtrack.service.ProjectService;
import com.example.buildtrack.service.SiteIssueService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/issues")
public class SiteIssueController {

    private final SiteIssueService issueService;
    private final ProjectService projectService;

    public SiteIssueController(
            SiteIssueService issueService,
            ProjectService projectService) {
        this.issueService = issueService;
        this.projectService = projectService;
    }

    @GetMapping
    public String listIssues(Model model) {
        model.addAttribute("issues", issueService.getAllIssues());
        return "issues";
    }

    @GetMapping("/new")
    public String newIssue(
            @RequestParam(required = false) Long projectId,
            Model model) {

        SiteIssue issue = new SiteIssue();
        issue.setProject(projectId == null
                ? new Project()
                : projectService.getProjectById(projectId));

        model.addAttribute("issue", issue);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Report Site Issue");
        return "issue-form";
    }

    @PostMapping
    public String createIssue(
            @Valid @ModelAttribute("issue") SiteIssue issue,
            BindingResult result,
            @RequestParam(value = "projectId", required = false)
            Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.rejectValue("project", "required",
                    "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Report Site Issue");
            return "issue-form";
        }

        issue.setProject(projectService.getProjectById(projectId));
        issueService.saveIssue(issue);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Site issue reported successfully.");

        return "redirect:/issues";
    }

    @GetMapping("/{id}/edit")
    public String editIssue(@PathVariable Long id, Model model) {
        model.addAttribute("issue", issueService.getIssueById(id));
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Edit Site Issue");
        return "issue-form";
    }

    @PostMapping("/{id}/edit")
    public String updateIssue(
            @PathVariable Long id,
            @Valid @ModelAttribute("issue") SiteIssue issue,
            BindingResult result,
            @RequestParam(value = "projectId", required = false)
            Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.rejectValue("project", "required",
                    "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Edit Site Issue");
            return "issue-form";
        }

        SiteIssue existing = issueService.getIssueById(id);
        existing.setTitle(issue.getTitle());
        existing.setDescription(issue.getDescription());
        existing.setSeverity(issue.getSeverity());
        existing.setStatus(issue.getStatus());
        existing.setReportedBy(issue.getReportedBy());
        existing.setProject(projectService.getProjectById(projectId));

        issueService.saveIssue(existing);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Site issue updated successfully.");

        return "redirect:/issues";
    }

    @PostMapping("/{id}/delete")
    public String deleteIssue(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        issueService.deleteIssue(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Site issue deleted successfully.");

        return "redirect:/issues";
    }
}
