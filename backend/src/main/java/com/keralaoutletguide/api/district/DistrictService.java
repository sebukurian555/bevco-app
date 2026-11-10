package com.keralaoutletguide.api.district;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
public class DistrictService {
    private static final Logger logger = LogManager.getLogger(DistrictService.class);

    private final DistrictsResponse districts;

    public DistrictService(JsonMapper jsonMapper) throws IOException {
        try (InputStream input = new ClassPathResource("districts.json").getInputStream()) {
            districts = jsonMapper.readValue(input, DistrictsResponse.class);
        }
        logger.info("Loaded {} districts from districts.json", districts.districts().size());
    }

    public DistrictsResponse listDistricts() {
        return districts;
    }
}
