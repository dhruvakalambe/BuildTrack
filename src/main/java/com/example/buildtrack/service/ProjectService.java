
package com.example.buildtrack.service;

import com.example.buildtrack.entity.Project;
import com.example.buildtrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Project not found: " + id));
    }

    public Project saveProject(Project project) {
        if (project.getStartDate() != null
                && project.getDeadline() != null
                && project.getDeadline().isBefore(project.getStartDate())) {
            throw new IllegalArgumentException(
                    "Deadline cannot be before the start date.");
        }

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public long getTotalProjects() {
        return projectRepository.count();
    }

    public long getActiveProjects() {
        return projectRepository.countByStatus("In Progress");
    }


    public boolean projectExists(Long id) {
        return projectRepository.existsById(id);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project existing = getProjectById(id);

        existing.setName(updatedProject.getName());
        existing.setLocation(updatedProject.getLocation());
        existing.setDescription(updatedProject.getDescription());
        existing.setBudget(updatedProject.getBudget());
        existing.setStartDate(updatedProject.getStartDate());
        existing.setDeadline(updatedProject.getDeadline());
        existing.setStatus(updatedProject.getStatus());

        return saveProject(existing);
    }


    public java.math.BigDecimal getTotalBudget() {
        return projectRepository.findAll()
                .stream()
                .map(Project::getBudget)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }


    public long getUpcomingDeadlineCount() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate nextWeek = today.plusDays(7);

        return projectRepository.findAll().stream()
                .filter(project -> project.getDeadline() != null)
                .filter(project -> !"Completed".equalsIgnoreCase(project.getStatus()))
                .filter(project -> !project.getDeadline().isBefore(today))
                .filter(project -> !project.getDeadline().isAfter(nextWeek))
                .count();
    }

    public long getOverdueProjectCount() {
        java.time.LocalDate today = java.time.LocalDate.now();

        return projectRepository.findAll().stream()
                .filter(project -> project.getDeadline() != null)
                .filter(project -> !"Completed".equalsIgnoreCase(project.getStatus()))
                .filter(project -> project.getDeadline().isBefore(today))
                .count();
    }

}
