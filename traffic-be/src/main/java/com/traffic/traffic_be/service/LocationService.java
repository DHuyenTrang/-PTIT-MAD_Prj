package com.traffic.traffic_be.service;

import com.traffic.traffic_be.dto.Request.LocationRequest;
import com.traffic.traffic_be.dto.Response.LocationResponse;

import java.util.List;

public interface LocationService {
    void addFavoriteLocation(LocationRequest request);
    List<LocationResponse> getMyFavoriteLocations();
    void saveSearchHistory(LocationRequest request);
    List<LocationResponse> getRecentSearches();
}
