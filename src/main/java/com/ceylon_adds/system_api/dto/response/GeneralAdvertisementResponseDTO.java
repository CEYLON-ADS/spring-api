package com.ceylon_adds.system_api.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAdvertisementResponseDTO {
    private UUID propertyId;
    private String title;
    private Boolean activeStatus;
    private Boolean whatsapp;
    private Boolean telegram;
    private Boolean imo;
    private Boolean viber;
    private Boolean isFake;
    private String[] cities;
    private List<ImageUrlResponseDTO> imageUrls;
    private String markedFakedBy;
    private Instant markedFakedAt;
    private int allLikes;
    private int allViews;
    private UUID userId;
    private String userMobileNumber;
    private UUID categoryId;
    private String categoryName;
    private Integer fakeCount;
    private String description;
    private Double price;
    private Instant createdDate;
}
