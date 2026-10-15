package com.example.helpdesk.repository;

import com.example.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByTitleContainingIgnoreCase(String title);

    List<Ticket> findByStatus(String status);

    List<Ticket> findByPriority(String priority);

    long countByStatus(String status);
}