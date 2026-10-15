
package com.example.buildtrack.service;

import com.example.buildtrack.entity.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectHealthService {

    private final TaskService taskService;
    private final SiteIssueService siteIssueService;
    private final MaterialService materialService;

    public ProjectHealthService(
            TaskService taskService,
            SiteIssueService siteIssueService,
            MaterialService materialService) {
        this.taskService = taskService;
        this.siteIssueService = siteIssueService;
        this.materialService = materialService;
    }

    public String getProjectHealth(Project project) {
        Long projectId = project.getId();

        if ("Completed".equalsIgnoreCase(project.getStatus())) {
            return "Completed";
        }

        long criticalIssues =
                siteIssueService.getCriticalIssueCountByProject(projectId);

        long overdueTasks =
                taskService.getOverdueTaskCountByProject(projectId);

        long materialShortages =
                materialService.getShortageCountByProject(projectId);

        boolean overdueProject = project.getDeadline() != null
                && project.getDeadline().isBefore(java.time.LocalDate.now());

        if (criticalIssues > 0 || overdueProject) {
            return "Critical";
        }

        if (overdueTasks > 0 || materialShortages > 0) {
            return "At Risk";
        }

        return "On Track";
    }
}
