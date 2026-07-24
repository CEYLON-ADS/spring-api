package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.dto.request.CityRequestDTO;
import com.ceylon_adds.system_api.dto.response.CityResponseDTO;
import com.ceylon_adds.system_api.dto.response.DistrictResponseDTO;
import com.ceylon_adds.system_api.dto.response.paginate.PaginateCityDTO;
import com.ceylon_adds.system_api.entity.City;
import com.ceylon_adds.system_api.entity.District;
import com.ceylon_adds.system_api.exception.EntryNotFoundException;
import com.ceylon_adds.system_api.repository.CityRepository;
import com.ceylon_adds.system_api.repository.DistrictRepository;
import com.ceylon_adds.system_api.service.CityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    @Override
    public void initializeCities() {
        if (cityRepository.count() == 0) {
            Map<String, List<String>> districtCities = new LinkedHashMap<>();

            districtCities.put("Colombo", Arrays.asList(
                    "Colombo", "Dehiwala-Mount Lavinia", "Moratuwa", "Kotte", "Battaramulla",
                    "Maharagama", "Homagama", "Kesbewa", "Kaduwela", "Kolonnawa", "Seethawaka"
            ));

            districtCities.put("Gampaha", Arrays.asList(
                    "Negombo", "Gampaha", "Katunayake", "Ja-Ela", "Minuwangoda",
                    "Wattala", "Ragama", "Divulapitiya", "Attanagalla", "Biyagama"
            ));

            districtCities.put("Kalutara", Arrays.asList(
                    "Kalutara", "Panadura", "Horana", "Beruwala", "Wadduwa", "Matugama", "Ingiriya", "Bulathsinhala"
            ));

            districtCities.put("Kandy", Arrays.asList(
                    "Kandy", "Gampola", "Nawalapitiya", "Kadugannawa", "Akurana", "Poojapitiya", "Harispattuwa"
            ));

            districtCities.put("Matale", Arrays.asList(
                    "Matale", "Dambulla", "Sigiriya", "Rattota", "Ukuwela"
            ));

            districtCities.put("Nuwara Eliya", Arrays.asList(
                    "Nuwara Eliya", "Hatton", "Talawakele", "Nanuoya", "Walapane"
            ));

            districtCities.put("Galle", Arrays.asList(
                    "Galle", "Hikkaduwa", "Ambalangoda", "Baddegama", "Elpitiya"
            ));

            districtCities.put("Matara", Arrays.asList(
                    "Matara", "Weligama", "Hakmana", "Dikwella"
            ));

            districtCities.put("Hambantota", Arrays.asList(
                    "Hambantota", "Tangalle", "Tissamaharama", "Beliatta"
            ));

            districtCities.put("Jaffna", Arrays.asList(
                    "Jaffna", "Nallur", "Chavakachcheri", "Point Pedro", "Karainagar"
            ));

            districtCities.put("Kilinochchi", Arrays.asList(
                    "Kilinochchi", "Pallai", "Poonakary", "Kandavalai"
            ));

            districtCities.put("Mannar", Arrays.asList(
                    "Mannar", "Nanaddan", "Madhu", "Musali"
            ));

            districtCities.put("Mullaitivu", Arrays.asList(
                    "Mullaitivu", "Oddusuddan", "Thunukkai", "Maritimepattu"
            ));

            districtCities.put("Vavuniya", Arrays.asList(
                    "Vavuniya", "Vavuniya North", "Vavuniya South", "Vengalacheddikulam"
            ));

            districtCities.put("Batticaloa", Arrays.asList(
                    "Batticaloa", "Eravur", "Kattankudy", "Kaluwanchikudy", "Valaichenai"
            ));

            districtCities.put("Ampara", Arrays.asList(
                    "Ampara", "Kalmunai", "Sainthamaruthu", "Akkaraipattu", "Uhana"
            ));

            districtCities.put("Trincomalee", Arrays.asList(
                    "Trincomalee", "Kinniya", "Mutur", "Kuchchaveli"
            ));

            districtCities.put("Kurunegala", Arrays.asList(
                    "Kurunegala", "Kuliyapitiya", "Pannala", "Mawathagama", "Nikaweratiya", "Polgahawela", "Galgamuwa"
            ));

            districtCities.put("Puttalam", Arrays.asList(
                    "Puttalam", "Chilaw", "Wennappuwa", "Anamaduwa", "Nattandiya"
            ));

            districtCities.put("Anuradhapura", Arrays.asList(
                    "Anuradhapura", "Kekirawa", "Medawachchiya", "Mihintale", "Nochchiyagama"
            ));

            districtCities.put("Polonnaruwa", Arrays.asList(
                    "Polonnaruwa", "Hingurakgoda", "Medirigiriya", "Thamankaduwa"
            ));

            districtCities.put("Badulla", Arrays.asList(
                    "Badulla", "Bandarawela", "Haputale", "Welimada", "Mahiyanganaya"
            ));

            districtCities.put("Monaragala", Arrays.asList(
                    "Monaragala", "Bibile", "Wellawaya", "Buttala"
            ));

            districtCities.put("Ratnapura", Arrays.asList(
                    "Ratnapura", "Balangoda", "Embilipitiya", "Kuruwita", "Pelmadulla"
            ));

            districtCities.put("Kegalle", Arrays.asList(
                    "Kegalle", "Mawanella", "Rambukkana", "Warakapola", "Deraniyagala"
            ));

            // --- Save cities in DB ---
            for (Map.Entry<String, List<String>> entry : districtCities.entrySet()) {
                District district = districtRepository.findAll().stream()
                        .filter(d -> d.getDistrict().equalsIgnoreCase(entry.getKey()))
                        .findFirst()
                        .orElse(null);

                if (district != null) {
                    List<City> cities = new ArrayList<>();
                    for (String cityName : entry.getValue()) {
                        cities.add(
                                City.builder()
                                        .name(cityName)
                                        .district(district)
                                        .build()
                        );
                    }
                    cityRepository.saveAll(cities);
                }
            }
        }
    }


    @Override
    public void create(CityRequestDTO dto) {
        District district = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new EntryNotFoundException("District not found"));

        for (String s : dto.getName().split(",")) {
            Optional<City> byName = cityRepository.findByName(s, dto.getDistrictId());
            if (byName.isEmpty()) {
                if (!s.trim().equals("")) {
                    cityRepository.save(
                            City.builder()
                                    .propertyId(UUID.randomUUID().toString())
                                    .name(s)
                                    .district(district)
                                    .build()
                    );
                }

            }
        }
    }

    @Override
    public void update(UUID cityId, CityRequestDTO dto) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new EntryNotFoundException("City not found"));

        District district = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new EntryNotFoundException("District not found"));

        city.setName(dto.getName());
        city.setDistrict(district);
        cityRepository.save(city);
    }

    @Override
    public void delete(UUID cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new EntityNotFoundException("City not found");
        }
        cityRepository.deleteById(cityId);
    }

    @Override
    public CityResponseDTO getById(UUID cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new EntryNotFoundException("City not found"));

        return CityResponseDTO.builder()
                .propertyID(city.getPropertyId().toString())
                .city(city.getName())
                .district(city.getDistrict().getDistrict())
                .build();
    }

    @Override
    public PaginateCityDTO search(String searchText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());

        Page<City> cityPage;
        if (searchText == null || searchText.trim().isEmpty()) {
            cityPage = cityRepository.findAll(pageable);
        } else {
            cityPage = cityRepository.searchCities(searchText, pageable);
        }

        return PaginateCityDTO.builder()
                .count(cityPage.getTotalElements())
                .dataList(cityPage.getContent().stream()
                        .map(c -> CityResponseDTO.builder()
                                .propertyID(c.getPropertyId())
                                .city(c.getName())
                                .district(c.getDistrict().getDistrict())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
