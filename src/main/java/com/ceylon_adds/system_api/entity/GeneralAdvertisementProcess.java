package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "general_advertisement_process")
public class GeneralAdvertisementProcess {

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

    @Column(name = "is_free_ad", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFreeAd = false;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Lob
    @Column(name = "description", columnDefinition = "LONGBLOB")
    private byte[] description;

    @Column(name = "service_fee")
    private Double serviceFee;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "views")
    private Integer views;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private ApplicationUser verifiedBy;

    @ManyToOne
    @JoinColumn(name = "advertise_id", nullable = false)
    private GeneralAdvertisement advertisement;

    @ManyToOne
    @JoinColumn(name = "advertise_type", nullable = false)
    private AdvertiseType advertiseType;

    @OneToMany(mappedBy = "ref", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GeneralAdvertisePaymentSlip> slips = new HashSet<>();


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
