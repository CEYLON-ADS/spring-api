package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_visit")
public class SystemVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "count", nullable = false)
    private Long count;

    @Column(name = "ref_date", nullable = false)
    private Instant refDate;

}
