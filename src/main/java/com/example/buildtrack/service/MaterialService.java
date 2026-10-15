
package com.example.buildtrack.service;

import com.example.buildtrack.entity.Material;
import com.example.buildtrack.repository.MaterialRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public List<Material> getMaterialsByProject(Long projectId) {
        return materialRepository.findByProjectId(projectId);
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Material not found: " + id));
    }

    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    public boolean isShortage(Material material) {
        return material.getQuantityInStock()
                .compareTo(material.getQuantityRequired()) < 0;
    }

    public BigDecimal getShortageQuantity(Material material) {
        BigDecimal shortage = material.getQuantityRequired()
                .subtract(material.getQuantityInStock());

        return shortage.max(BigDecimal.ZERO);
    }

    public long getShortageCount() {
        return materialRepository.findAll().stream()
                .filter(this::isShortage)
                .count();
    }


    public long getShortageCountByProject(Long projectId) {
        return materialRepository.findByProjectId(projectId).stream()
                .filter(material -> material.getQuantityInStock() != null)
                .filter(material -> material.getQuantityRequired() != null)
                .filter(material -> material.getQuantityInStock()
                        .compareTo(material.getQuantityRequired()) < 0)
                .count();
    }

}
