
package com.example.buildtrack.repository;

import com.example.buildtrack.entity.Material;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Override
    @EntityGraph(attributePaths = {"project"})
    List<Material> findAll();

    @Override
    @EntityGraph(attributePaths = {"project"})
    Optional<Material> findById(Long id);

    List<Material> findByProjectId(Long projectId);

    List<Material> findByNameContainingIgnoreCase(String name);
}
