package com.ceylon_adds.system_api;

import com.ceylon_adds.system_api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class SystemApiApplication implements CommandLineRunner {

	private final ApplicationUserRoleService applicationUserRoleService;
	private final ApplicationUserService applicationUserService;
	private final AdvertiseTypeService advertiseTypeService;
	private final DistrictService districtService;
	private final CityService cityService;


	public static void main(String[] args) {
		SpringApplication.run(SystemApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		applicationUserRoleService.initializeRoles();
		applicationUserService.initializeSystemHost();
		advertiseTypeService.initializeTypes();
	}
}
