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
@Table(name = "slot_ad")
public class SlotAd {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "redirect_link")
    private String redirectLink;

    @Column(name = "active_status", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean activeStatus = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private AdvertisementSlot slot;

    @OneToMany(mappedBy = "slotAd", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SlotAdvertisementImage> slotAdvertisementImages = new HashSet<>();


    @OneToMany(mappedBy = "slotAdvertisement", fetch = FetchType.EAGER)
    private Set<SlotAdvertisementProcess> slotAdvertisementProcesses = new HashSet<>();


}