package com.ceylon_adds.system_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "property_id", nullable = false, length = 80)
    private String propertyId;

    @Column(name = "active_state", columnDefinition = "TINYINT")
    private Boolean activeState;

    @Column(name = "capital", length = 100, nullable = false)
    private String capital;

    @Column(name = "continent_code", length = 45, nullable = false)
    private String continentCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME")
    private Date createdDate;

    @Column(name = "continent_name", length = 100, nullable = false)
    private String continentName;

    @Column(name = "dial_code", length = 100, nullable = false)
    private String dialCode;

    @Column(name = "country_code", length = 45, nullable = false)
    private String countryCode;

    @Column(name = "country_name", length = 100, nullable = false, unique = true)
    private String countryName;

    @Column(name = "currency_code", length = 80, nullable = false)
    private String currencyCode;

    @Column(name = "currency_name", length = 100, nullable = false)
    private String currencyName;

    @Column(name = "currency_symbol", length = 80,nullable = false)
    private String currencySymbol;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<District> districts = new HashSet<>();
}
