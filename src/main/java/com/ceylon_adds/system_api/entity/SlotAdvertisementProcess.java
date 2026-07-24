package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "slot_advertisement_process")
public class SlotAdvertisementProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "verified_status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean verifiedStatus = false;

    @Column(name = "active_status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean activeStatus = false;

    @Column(name = "advertise_cost")
    private Double advertiseCost;

    @Column(name = "is_free_ad", columnDefinition = "TINYINT")
    private Boolean isFreeAd;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(name = "views")
    private Integer views;

    @ManyToOne
    @JoinColumn(name = "slot_advertise_id", nullable = false)
    private SlotAd slotAdvertisement;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private ApplicationUser verifiedBy;

    @OneToMany(mappedBy = "ref")
    private Set<SlotAdvertisePaymentSlip> slips;


    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
        updatedDate = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = Instant.now();
    }
}
