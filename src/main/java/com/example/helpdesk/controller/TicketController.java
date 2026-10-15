package com.example.helpdesk.controller;

import com.example.helpdesk.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.helpdesk.entity.Ticket;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Model model) {

        model.addAttribute(
                "tickets",
                ticketService.searchTickets(keyword, status, priority)
        );

        model.addAttribute("totalTickets",
                ticketService.getAllTickets().size());

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);

        model.addAttribute("totalTickets", ticketService.getTotalTickets());
        model.addAttribute("openTickets", ticketService.getOpenTickets());
        model.addAttribute("inProgressTickets", ticketService.getInProgressTickets());
        model.addAttribute("resolvedTickets", ticketService.getResolvedTickets());

        return "dashboard";
    }

    @GetMapping("/tickets/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "create-ticket";
    }

    @PostMapping("/tickets")
    public String createTicket(
            @Valid @ModelAttribute("ticket") Ticket ticket,
            BindingResult result) {

        if (result.hasErrors()) {
            return "create-ticket";
        }

        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

    @GetMapping("/tickets/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket-details";
    }


    @PostMapping("/tickets/{id}/status")
    public String updateTicketStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (!java.util.Set.of("Open", "In Progress", "Resolved")
                .contains(status)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid ticket status.");
            return "redirect:/tickets/" + id;
        }

        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(status);
        ticketService.saveTicket(ticket);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Ticket status updated successfully.");

        return "redirect:/tickets/" + id;
    }

}
