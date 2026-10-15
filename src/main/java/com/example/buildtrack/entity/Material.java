
package com.example.buildtrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal quantityInStock = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal quantityRequired = BigDecimal.ZERO;

    @NotBlank
    private String unit;

    private String supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Material() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(BigDecimal quantityInStock) { this.quantityInStock = quantityInStock; }
    public BigDecimal getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(BigDecimal quantityRequired) { this.quantityRequired = quantityRequired; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}
