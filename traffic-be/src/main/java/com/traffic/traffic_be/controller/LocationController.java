package com.traffic.traffic_be.controller;

import com.traffic.traffic_be.dto.Request.LocationRequest;
import com.traffic.traffic_be.dto.Response.ApiResponse;
import com.traffic.traffic_be.dto.Response.LocationResponse;
import com.traffic.traffic_be.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Location", description = "Quản lý Địa điểm yêu thích và Lịch sử tìm kiếm")
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/favorite")
    @Operation(summary = "Lưu địa điểm vào danh sách yêu thích (Cần Token)")
    public ResponseEntity<ApiResponse<String>> addFavorite(@Valid @RequestBody LocationRequest request) {
        locationService.addFavoriteLocation(request);
        return ResponseEntity.ok(ApiResponse.success("Đã thêm vào danh sách yêu thích!", null));
    }

    @GetMapping("/favorite")
    @Operation(summary = "Lấy danh sách địa điểm yêu thích của tôi (Cần Token)")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getFavorites() {
        return ResponseEntity.ok(ApiResponse.success(locationService.getMyFavoriteLocations()));
    }

    @PostMapping("/history")
    @Operation(summary = "Lưu vào lịch sử tìm kiếm (Cần Token)")
    public ResponseEntity<ApiResponse<String>> saveSearchHistory(@Valid @RequestBody LocationRequest request) {
        locationService.saveSearchHistory(request);
        return ResponseEntity.ok(ApiResponse.success("Đã lưu vào lịch sử tìm kiếm!", null));
    }

    @GetMapping("/recent")
    @Operation(summary = "Lấy danh sách tìm kiếm gần đây (Cần Token)")
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getRecent() {
        return ResponseEntity.ok(ApiResponse.success(locationService.getRecentSearches()));
    }
}
