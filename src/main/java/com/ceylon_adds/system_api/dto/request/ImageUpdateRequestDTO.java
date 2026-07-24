package com.ceylon_adds.system_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUpdateRequestDTO {

    private UUID propertyId;
//    private String oldUrl;
    private MultipartFile image;
}
