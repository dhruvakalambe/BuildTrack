
package com.example.buildtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "site_issues")
public class SiteIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    private String severity = "Medium";

    @NotBlank
    private String status = "Open";

    @NotBlank
    private String reportedBy;

    private LocalDateTime reportedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public SiteIssue() {}

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}
