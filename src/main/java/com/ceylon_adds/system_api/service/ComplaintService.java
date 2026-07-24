package com.ceylon_adds.system_api.service;

import com.ceylon_adds.system_api.dto.request.ComplaintRemarkRequestDTO;
import com.ceylon_adds.system_api.dto.request.ComplaintRequestDTO;
import com.ceylon_adds.system_api.dto.response.ComplaintResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateComplaintDTO;

import java.util.UUID;

public interface ComplaintService {

    void create(ComplaintRequestDTO dto);

    void update(UUID complaintId, ComplaintRequestDTO dto);

    void delete(UUID complaintId);

    void remark(UUID complaintId, ComplaintRemarkRequestDTO dto);

    ComplaintResponseDTO getById(UUID complaintId);

    PaginateComplaintDTO search(String searchText, int page, int pageSize);

}
