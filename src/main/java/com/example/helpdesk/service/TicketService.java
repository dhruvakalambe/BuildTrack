
package com.example.helpdesk.service;

import com.example.helpdesk.entity.Ticket;
import com.example.helpdesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Ticket not found: " + id));
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> searchTickets(
            String keyword, String status, String priority) {

        String searchKeyword =
                keyword == null ? "" : keyword.trim();

        boolean hasStatus = status != null && !status.isBlank();
        boolean hasPriority = priority != null && !priority.isBlank();

        return ticketRepository.findAll().stream()
                .filter(ticket ->
                        ticket.getTitle() != null &&
                                ticket.getTitle().toLowerCase()
                                        .contains(searchKeyword.toLowerCase()))
                .filter(ticket ->
                        !hasStatus || status.equals(ticket.getStatus()))
                .filter(ticket ->
                        !hasPriority || priority.equals(ticket.getPriority()))
                .toList();
    }

    public long getTotalTickets() {
        return ticketRepository.count();
    }

    public long getOpenTickets() {
        return ticketRepository.countByStatus("Open");
    }

    public long getInProgressTickets() {
        return ticketRepository.countByStatus("In Progress");
    }

    public long getResolvedTickets() {
        return ticketRepository.countByStatus("Resolved");
    }
}
