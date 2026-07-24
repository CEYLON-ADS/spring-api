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
@Table(name = "district")
public class District {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String propertyId;

    @Column(name = "district", nullable = false, unique = true)
    private String district;

    @OneToMany(mappedBy = "district")
    private Set<City> cities = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

}
