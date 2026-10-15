
package com.example.buildtrack.repository;

import com.example.buildtrack.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    @EntityGraph(attributePaths = {"project"})
    Optional<Task> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"project"})
    List<Task> findAll();

    List<Task> findByProjectId(Long projectId);

    List<Task> findByStatus(String status);

    long countByStatus(String status);

    long countByProjectIdAndStatus(Long projectId, String status);

    long countByProjectId(Long projectId);
}