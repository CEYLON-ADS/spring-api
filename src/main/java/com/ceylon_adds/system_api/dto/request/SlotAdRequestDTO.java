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
public class SlotAdRequestDTO {

    private String redirectLink;
    private UUID slotId;
    private UUID userId;
    private List<MultipartFile> images;

}
