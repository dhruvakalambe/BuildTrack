
package com.example.buildtrack.service;

import com.example.buildtrack.entity.SiteIssue;
import com.example.buildtrack.repository.SiteIssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteIssueService {

    private final SiteIssueRepository siteIssueRepository;

    public SiteIssueService(SiteIssueRepository siteIssueRepository) {
        this.siteIssueRepository = siteIssueRepository;
    }

    public List<SiteIssue> getAllIssues() {
        return siteIssueRepository.findAll();
    }

    public List<SiteIssue> getIssuesByProject(Long projectId) {
        return siteIssueRepository.findByProjectId(projectId);
    }

    public SiteIssue getIssueById(Long id) {
        return siteIssueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Site issue not found: " + id));
    }

    public SiteIssue saveIssue(SiteIssue issue) {
        return siteIssueRepository.save(issue);
    }

    public void deleteIssue(Long id) {
        siteIssueRepository.deleteById(id);
    }

    public long getOpenIssueCount() {
        return siteIssueRepository.countByStatus("Open");
    }

    public long getCriticalIssueCount() {
        return siteIssueRepository.countBySeverityAndStatus(
                "Critical", "Open");
    }


    public long getCriticalIssueCountByProject(Long projectId) {
        return siteIssueRepository.findByProjectId(projectId).stream()
                .filter(issue -> "Critical".equalsIgnoreCase(issue.getSeverity()))
                .filter(issue -> !"Resolved".equalsIgnoreCase(issue.getStatus()))
                .count();
    }

}
