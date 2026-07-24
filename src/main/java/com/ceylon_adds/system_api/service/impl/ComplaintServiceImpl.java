package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.ComplaintRemarkRequestDTO;
import com.ceylon_adds.system_api.dto.request.ComplaintRequestDTO;
import com.ceylon_adds.system_api.dto.response.ComplaintResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateComplaintDTO;
import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.entity.Complaint;
import com.ceylon_adds.system_api.entity.GeneralAdvertisement;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import com.ceylon_adds.system_api.repository.ComplaintRepository;
import com.ceylon_adds.system_api.repository.GeneralAdvertisementRepository;
import com.ceylon_adds.system_api.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final GeneralAdvertisementRepository generalAdvertisementRepository;
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public void create(ComplaintRequestDTO dto) {
        GeneralAdvertisement ad = generalAdvertisementRepository.findById(dto.getGeneralAdId())
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        Complaint complaint = Complaint.builder()
                .message(dto.getMessage())
                .generalAdvertisement(ad)
                .build();

        complaintRepository.save(complaint);
    }

    @Override
    public void update(UUID complaintId, ComplaintRequestDTO dto) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntryNotFoundException("Complaint not found"));

        GeneralAdvertisement ad = generalAdvertisementRepository.findById(dto.getGeneralAdId())
                .orElseThrow(() -> new EntryNotFoundException("General advertisement not found"));

        complaint.setMessage(dto.getMessage());

        complaintRepository.save(complaint);
    }

    @Override
    public void delete(UUID complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntryNotFoundException("Complaint not found"));
        complaintRepository.delete(complaint);
    }

    @Override
    public void remark(UUID complaintId, ComplaintRemarkRequestDTO dto) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntryNotFoundException("Complaint not found"));

        ApplicationUser user = applicationUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntryNotFoundException("User not found"));

        complaint.setRemark(dto.getRemark());
        complaint.setManagedBy(user);

        complaintRepository.save(complaint);
    }

    @Override
    public ComplaintResponseDTO getById(UUID complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntryNotFoundException("Complaint not found"));

        return mapToResponse(complaint);
    }

    @Override
    public PaginateComplaintDTO search(String searchText, int page, int pageSize) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Complaint> complaintPage;

        if (searchText == null || searchText.trim().isEmpty()) {
            complaintPage = complaintRepository.findAll(pageRequest);
        } else {

            //TODO : need to implement the search according to requirements.Remove the below test code
            complaintPage = complaintRepository.findAll(pageRequest);
        }

        return PaginateComplaintDTO.builder()
                .count(complaintPage.getTotalElements())
                .dataList(
                        complaintPage.stream().map(this::mapToResponse).toList()
                )
                .build();
    }


    private ComplaintResponseDTO mapToResponse(Complaint complaint) {
        return ComplaintResponseDTO.builder()
                .propertyId(complaint.getPropertyId())
                .message(complaint.getMessage())
                .remark(complaint.getRemark())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .managedBy(
                        complaint.getManagedBy() != null ? complaint.getManagedBy().getPropertyId() : null
                )
                .generalAdId(complaint.getGeneralAdvertisement().getPropertyId())
                .build();
    }
}
