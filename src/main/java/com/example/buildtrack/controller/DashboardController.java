
package com.example.buildtrack.controller;

import com.example.buildtrack.service.MaterialService;
import com.example.buildtrack.service.ProjectService;
import com.example.buildtrack.service.SiteIssueService;
import com.example.buildtrack.service.TaskService;
import com.example.buildtrack.service.ProjectHealthService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DashboardController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final SiteIssueService siteIssueService;
    private final MaterialService materialService;
    private final ProjectHealthService projectHealthService;


    public DashboardController(
            ProjectService projectService,
            TaskService taskService,
            SiteIssueService siteIssueService,
            MaterialService materialService,
            ProjectHealthService projectHealthService) {

        this.projectService = projectService;
        this.taskService = taskService;
        this.siteIssueService = siteIssueService;
        this.materialService = materialService;
        this.projectHealthService = projectHealthService;
    }


    @GetMapping("/")
    public String dashboard(Model model) {


        Map<Long, Integer> projectProgress = new HashMap<>();

        for (var project : projectService.getAllProjects()) {
            projectProgress.put(
                    project.getId(),
                    taskService.getProjectProgress(project.getId())
            );
        }


        Map<Long, String> projectHealth = new HashMap<>();

        for (var project : projectService.getAllProjects()) {
            projectHealth.put(
                    project.getId(),
                    projectHealthService.getProjectHealth(project)
            );
        }

        model.addAttribute("projectHealth", projectHealth);


        model.addAttribute("projectProgress", projectProgress);

        model.addAttribute("totalBudget", projectService.getTotalBudget());

        model.addAttribute("upcomingDeadlines",
                projectService.getUpcomingDeadlineCount());

        model.addAttribute("overdueProjects",
                projectService.getOverdueProjectCount());

        model.addAttribute("totalProjects",
                projectService.getTotalProjects());

        model.addAttribute("activeProjects",
                projectService.getActiveProjects());

        model.addAttribute("overdueTasks",
                taskService.getOverdueTaskCount());

        model.addAttribute("criticalIssues",
                siteIssueService.getCriticalIssueCount());

        model.addAttribute("materialShortages",
                materialService.getShortageCount());

        model.addAttribute("projects",
                projectService.getAllProjects());

        return "dashboard";
    }
}
