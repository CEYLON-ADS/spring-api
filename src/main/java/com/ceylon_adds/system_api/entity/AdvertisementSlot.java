package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advertisement_slot")
public class AdvertisementSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "estimate_cost")
    private Double estimateCost;

    @Column(name = "active_state", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean activeState = false;

    @Column(name = "slot_number", unique = true, nullable = false)
    private Integer slotNumber;

    @Column(name = "availability", columnDefinition = "TINYINT")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private Set<SlotAd> slotAdvertisements = new HashSet<>();

}
