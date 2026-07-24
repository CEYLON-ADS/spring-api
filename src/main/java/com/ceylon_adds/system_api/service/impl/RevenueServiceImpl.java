package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.response.RevenueResponseDTO;
import com.ceylon_adds.system_api.entity.Category;
import com.ceylon_adds.system_api.entity.GeneralAdvertisementProcess;
import com.ceylon_adds.system_api.entity.SlotAdvertisementProcess;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.*;
import com.ceylon_adds.system_api.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final CategoryRepository categoryRepository;
    private final GeneralAdvertisementRepository generalAdvertisementRepository;
    private final GeneralAdvertisementProcessRepository generalAdvertisementProcessRepository;
    private final SlotAdvertisementProcessRepository slotAdvertisementProcessRepository;
    private final SlotAdRepository slotAdRepository;
    private final AdvertisementSlotRepository advertisementSlotRepository;

    @Override
    public List<RevenueResponseDTO> getRevenue(UUID categoryId, LocalDate startDate, LocalDate endDate) {

        List<RevenueResponseDTO> genAdRevenue = new ArrayList<>();
        List<RevenueResponseDTO> slotAdRevenue = new ArrayList<>();

        if (categoryId == null){
            // General Ads Revenue
            generalAdvertisementRepository.findAll()
                    .forEach(generalAdvertisement -> {
                        generalAdvertisementProcessRepository.findAllByAdvertisementAndCreatedDateBetween(
                                generalAdvertisement,
                                startDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                                endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1)
                        ).forEach(process -> {
                            LocalDate date = Optional.ofNullable(process.getCreatedDate())
                                    .map(d -> d.atZone(ZoneOffset.UTC).toLocalDate())
                                    .orElse(null);

                            Double revenue = Optional.ofNullable(process.getAdvertiseCost()).orElse(0.0);

                            if (date != null) {  // only add if date is valid
                                genAdRevenue.add(
                                        RevenueResponseDTO.builder()
                                                .date(date)
                                                .revenue(revenue)
                                                .build()
                                );
                            }
                        });

                    });

            // Slot Ads Revenue
            advertisementSlotRepository.findAll()
                    .forEach(advertisementSlot -> advertisementSlot.getSlotAdvertisements().forEach(slotAdvertisement -> {
                        slotAdvertisementProcessRepository.findAllBySlotAdvertisementAndCreatedDateBetween(
                                slotAdvertisement,
                                startDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                                endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1)
                        ).forEach(process -> {
                            LocalDate date = Optional.ofNullable(process.getCreatedDate())
                                    .map(d -> d.atZone(ZoneOffset.UTC).toLocalDate())
                                    .orElse(null);

                            Double revenue = Optional.ofNullable(process.getAdvertiseCost()).orElse(0.0);

                            if (date != null) {  // only add if date is valid
                                slotAdRevenue.add(
                                        RevenueResponseDTO.builder()
                                                .date(date)
                                                .revenue(revenue)
                                                .build()
                                );
                            }
                        });

                    }));
        }else {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntryNotFoundException("Category not found"));
            // General Ads Revenue
            generalAdvertisementRepository.findAllByCategory(category)
                    .forEach(generalAdvertisement -> {
                        generalAdvertisementProcessRepository.findAllByAdvertisementAndCreatedDateBetween(
                                generalAdvertisement,
                                startDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                                endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1)
                        ).forEach(process -> {
                            LocalDate date = Optional.ofNullable(process.getCreatedDate())
                                    .map(d -> d.atZone(ZoneOffset.UTC).toLocalDate())
                                    .orElse(null);

                            Double revenue = Optional.ofNullable(process.getAdvertiseCost()).orElse(0.0);

                            if (date != null) {  // only add if date is valid
                                genAdRevenue.add(
                                        RevenueResponseDTO.builder()
                                                .date(date)
                                                .revenue(revenue)
                                                .build()
                                );
                            }
                        });
                    });

            // Slot Ads Revenue
            advertisementSlotRepository.findAllByCategory(category)
                    .forEach(advertisementSlot -> advertisementSlot.getSlotAdvertisements().forEach(slotAdvertisement -> {
                        slotAdvertisementProcessRepository.findAllBySlotAdvertisementAndCreatedDateBetween(
                                slotAdvertisement,
                                startDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                                endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1)
                        ).forEach(process -> {
                            LocalDate date = Optional.ofNullable(process.getCreatedDate())
                                    .map(d -> d.atZone(ZoneOffset.UTC).toLocalDate())
                                    .orElse(null);

                            Double revenue = Optional.ofNullable(process.getAdvertiseCost()).orElse(0.0);

                            if (date != null) {  // only add if date is valid
                                slotAdRevenue.add(
                                        RevenueResponseDTO.builder()
                                                .date(date)
                                                .revenue(revenue)
                                                .build()
                                );
                            }
                        });
                    }));
        }




        Map<LocalDate, Double> revenueByDate = new HashMap<>();
        genAdRevenue.forEach(r -> {
            if (r.getDate() != null) {
                revenueByDate.merge(r.getDate(),
                        Optional.ofNullable(r.getRevenue()).orElse(0.0),
                        Double::sum);
            }
        });
        slotAdRevenue.forEach(r -> {
            if (r.getDate() != null) {
                revenueByDate.merge(r.getDate(),
                        Optional.ofNullable(r.getRevenue()).orElse(0.0),
                        Double::sum);
            }
        });

//        slotAdRevenue.forEach(r -> revenueByDate.merge(r.getDate(), r.getRevenue(), Double::sum));


        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            revenueByDate.putIfAbsent(current, 0.0);
            current = current.plusDays(1);
        }


        return revenueByDate.entrySet().stream()
                .map(entry -> RevenueResponseDTO.builder()
                        .date(entry.getKey())
                        .revenue(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(RevenueResponseDTO::getDate))
                .toList();
    }



    @Override
    public Double getTotalRevenue() {

        return generalAdvertisementProcessRepository.findAll().stream()
                .mapToDouble(GeneralAdvertisementProcess::getAdvertiseCost)
                .sum()
                + slotAdvertisementProcessRepository.findAll().stream()
                .mapToDouble(SlotAdvertisementProcess::getAdvertiseCost)
                .sum();

    }
}
