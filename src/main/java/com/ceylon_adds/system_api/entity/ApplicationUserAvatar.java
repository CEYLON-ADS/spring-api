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
@Table(name = "application_user_avatar")
public class ApplicationUserAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID propertyId;

    @Lob
    @Column(name = "directory", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] directory;

    @Lob
    @Column(name = "file_name", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileName;

    @Lob
    @Column(name = "hash", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] hash;

    @Lob
    @Column(name = "resource_url", columnDefinition = "LONGBLOB")
    private byte[] resourceUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

}