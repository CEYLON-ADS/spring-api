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
@Table(name = "application_user")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Column(name = "mobile_number", unique = true, nullable = false, length = 15)
    private String mobileNumber;

    @Column(name = "active_state", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean activeState = true;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToOne(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private OTP otp;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApplicationUserAvatar avatar;

    @OneToMany(mappedBy = "user")
    private Set<GeneralAdvertisement> generalAdvertisements = new HashSet<>();

    @OneToMany(mappedBy = "markedFakeBy")
    private Set<GeneralAdvertisement> fakedGeneralAdvertisements = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SlotAd> slotAdvertisements = new HashSet<>();

    @OneToMany(mappedBy = "verifiedBy")
    private Set<SlotAdvertisementProcess> slotAdvertisementProcesses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SavedAd> savedAds = new HashSet<>();

    @OneToMany(mappedBy = "managedBy")
    private Set<Complaint>  complains = new HashSet<>();

    @OneToMany(mappedBy = "verifiedBy")
    private Set<GeneralAdvertisementProcess>  advertisementProcesses = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "user_has_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<ApplicationUserRole> roles = new HashSet<>();


    /** Automatically handle timestamps **/
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}
