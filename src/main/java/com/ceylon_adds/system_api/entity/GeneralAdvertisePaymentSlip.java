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
@Table(name = "payment_slip")
public class GeneralAdvertisePaymentSlip {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_id", nullable = false)
    private GeneralAdvertisementProcess ref;

}
