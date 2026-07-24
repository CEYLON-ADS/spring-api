package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAdvertisementByAdminRequestDTO {

    private String title;
    private Boolean whatsapp;
    private Boolean telegram;
    private Boolean viber;
    private Boolean imo;
    private Boolean verify;
    private UUID categoryID;
    private List<UUID> cityIds;
    private String mobileNumber;
    private String countryCode;
    private UUID adType;
    private String description;
    private Double serviceFee;
    private List<MultipartFile> images;
    private List<MultipartFile> slips;

}
