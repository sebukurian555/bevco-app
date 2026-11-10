package com.keralaoutletguide.api.district;

import java.util.List;

public record DistrictsResponse(List<District> districts) {
    public DistrictsResponse {
        districts = List.copyOf(districts);
    }
}
