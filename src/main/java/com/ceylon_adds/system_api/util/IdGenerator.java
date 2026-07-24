package com.ceylon_adds.system_api.util;

import com.ceylon_adds.system_api.entity.ApplicationUser;
import com.ceylon_adds.system_api.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    public Integer generateUserAccountId(ApplicationUserRepository applicationUserRepository) {


        Integer lastAccountId =  applicationUserRepository.findFirstByOrderByCreatedAtDesc()
                .map(ApplicationUser::getAccountId)
                .orElse(null);

        if (lastAccountId == null) return 1000;
        return lastAccountId + 1;


    }
}
