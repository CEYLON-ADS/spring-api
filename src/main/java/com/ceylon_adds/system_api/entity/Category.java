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
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID propertyId;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "active_status", nullable = false, columnDefinition = "TINYINT")
    private Boolean activeStatus;

    @OneToMany(mappedBy = "category")
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<GeneralAdvertisement> generalAdvertisements = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<AdvertisementSlot> advertisementSlots = new HashSet<>();

}
