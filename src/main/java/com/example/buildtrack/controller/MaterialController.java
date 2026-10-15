
package com.example.buildtrack.controller;

import com.example.buildtrack.entity.Material;
import com.example.buildtrack.entity.Project;
import com.example.buildtrack.service.MaterialService;
import com.example.buildtrack.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final ProjectService projectService;

    public MaterialController(MaterialService materialService,
                              ProjectService projectService) {
        this.materialService = materialService;
        this.projectService = projectService;
    }

    @GetMapping
    public String listMaterials(Model model) {
        model.addAttribute("materials", materialService.getAllMaterials());
        return "materials";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("material", new Material());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Add Material");
        return "material-form";
    }

    @PostMapping
    public String createMaterial(
            @Valid @ModelAttribute("material") Material material,
            BindingResult result,
            @RequestParam(value = "projectId", required = false) Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.reject("project", "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Add Material");
            return "material-form";
        }

        Project project = projectService.getProjectById(projectId);

        if (project == null) {
            result.reject("project", "Selected project was not found.");
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Add Material");
            return "material-form";
        }

        material.setProject(project);
        materialService.saveMaterial(material);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Material added successfully!");

        return "redirect:/materials";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Material material = materialService.getMaterialById(id);

        model.addAttribute("material", material);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("pageTitle", "Edit Material");

        return "material-form";
    }

    @PostMapping("/{id}/edit")
    public String updateMaterial(
            @PathVariable Long id,
            @Valid @ModelAttribute("material") Material formMaterial,
            BindingResult result,
            @RequestParam(value = "projectId", required = false) Long projectId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (projectId == null) {
            result.reject("project", "Please select a project.");
        }

        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("pageTitle", "Edit Material");
            return "material-form";
        }

        Material existing = materialService.getMaterialById(id);
        Project project = projectService.getProjectById(projectId);

        existing.setName(formMaterial.getName());
        existing.setQuantityInStock(formMaterial.getQuantityInStock());
        existing.setQuantityRequired(formMaterial.getQuantityRequired());
        existing.setUnit(formMaterial.getUnit());
        existing.setSupplier(formMaterial.getSupplier());
        existing.setProject(project);

        materialService.saveMaterial(existing);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Material updated successfully!");

        return "redirect:/materials";
    }

    @PostMapping("/{id}/delete")
    public String deleteMaterial(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        materialService.deleteMaterial(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Material deleted successfully!");

        return "redirect:/materials";
    }
}
