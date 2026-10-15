
package com.example.buildtrack.repository;

import com.example.buildtrack.entity.SiteIssue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteIssueRepository
        extends JpaRepository<SiteIssue, Long> {

    @Override
    @EntityGraph(attributePaths = {"project"})
    List<SiteIssue> findAll();

    @Override
    @EntityGraph(attributePaths = {"project"})
    Optional<SiteIssue> findById(Long id);

    List<SiteIssue> findByProjectId(Long projectId);

    List<SiteIssue> findByStatus(String status);

    long countByStatus(String status);

    long countBySeverityAndStatus(String severity, String status);
}
