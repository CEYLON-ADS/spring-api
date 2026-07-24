package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advertise_type")
public class AdvertiseType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price")
    private Double price;
}
