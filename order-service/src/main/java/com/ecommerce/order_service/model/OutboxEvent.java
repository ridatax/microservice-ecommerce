package com.ecommerce.order_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateId; // Guardaremos el orderNumber
    private String type;        // Identificador del evento (ORDER_PLACED)

    @Column(columnDefinition = "TEXT")
    private String payload;     // El objeto convertido a JSON String

    private LocalDateTime createdAt;
    private boolean processed;  // Estado para el futuro proceso de envío
}
