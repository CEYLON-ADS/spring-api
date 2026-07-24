package com.ceylon_adds.system_api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "general_advertisement")
public class GeneralAdvertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "active_status", nullable = false, columnDefinition = "TINYINT")
    private Boolean activeStatus;

    @Column(name = "whatsapp", nullable = false, columnDefinition = "TINYINT")
    private Boolean whatsapp;

    @Column(name = "telegram", nullable = false, columnDefinition = "TINYINT")
    private Boolean telegram;

    @Column(name = "imo", nullable = false, columnDefinition = "TINYINT")
    private Boolean imo;

    @Column(name = "viber", nullable = false, columnDefinition = "TINYINT")
    private Boolean viber;

    @Column(name = "fake_count")
    private Integer fakeCount;

    @Column(name = "is_fake", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isFake = false;

    @Column(name = "marked_fake_date")
    private Instant markedFakeDate;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "marked_fake_by")
    private ApplicationUser markedFakeBy;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<GeneralAdvertisementProcess> generalAdvertisementProcess = new HashSet<>();

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SavedAd> savedAds = new HashSet<>();

    @OneToMany(mappedBy = "generalAdvertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AdvertiseImage> advertiseImages = new HashSet<>();

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GeneralAdAvCity> cities = new HashSet<>();

    @OneToMany(mappedBy = "generalAdvertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Complaint> complaints = new HashSet<>();


    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
    }

}
