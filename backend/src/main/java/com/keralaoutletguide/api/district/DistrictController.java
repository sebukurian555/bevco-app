package com.keralaoutletguide.api.district;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictController {
    private static final Logger logger = LogManager.getLogger(DistrictController.class);

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/api/v1/districts")
    public DistrictsResponse listDistricts() {
        logger.info("Received request to list districts");
        return districtService.listDistricts();
    }
}
