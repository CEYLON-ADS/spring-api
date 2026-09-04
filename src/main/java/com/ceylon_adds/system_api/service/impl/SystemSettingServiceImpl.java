package com.ceylon_adds.system_api.service.impl;

import com.ceylon_adds.system_api.entity.SystemSetting;
import com.ceylon_adds.system_api.repository.SystemSettingRepository;
import com.ceylon_adds.system_api.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    public static final String CREDIT_COST_KEY = "CREDIT_COST_PER_AD";
    private final SystemSettingRepository systemSettingRepository;

    @Override
    @Transactional(readOnly = true)
    public Double getCreditCostPerAd() {
        return systemSettingRepository.findBySettingKey(CREDIT_COST_KEY)
                .map(setting -> {
                    try {
                        return Double.parseDouble(setting.getSettingValue());
                    } catch (NumberFormatException e) {
                        return 1.0;
                    }
                })
                .orElse(1.0);
    }

    @Override
    @Transactional
    public void setCreditCostPerAd(Double cost) {
        SystemSetting setting = systemSettingRepository.findBySettingKey(CREDIT_COST_KEY)
                .orElseGet(() -> SystemSetting.builder()
                        .settingKey(CREDIT_COST_KEY)
                        .description("Credits deducted per approved advertisement")
                        .build());
        setting.setSettingValue(String.valueOf(cost));
        systemSettingRepository.save(setting);
    }
}
