package com.traffic.traffic_be.controller;

import com.traffic.traffic_be.dto.Request.AddLicensePlateRequest;
import com.traffic.traffic_be.dto.Request.DeleteLicensePlateRequest;
import com.traffic.traffic_be.dto.Response.ApiResponse;
import com.traffic.traffic_be.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v5/api/car")
@RequiredArgsConstructor
@Tag(name = "Car", description = "Quản lý biển số xe theo user")
public class CarController {

    private final CarService carService;

    @PostMapping("/add")
    @Operation(summary = "Thêm biển số xe (Cần Token)")
    public ResponseEntity<ApiResponse<Void>> add(@Valid @RequestBody AddLicensePlateRequest request) {
        carService.addLicensePlate(request.getLicensePlate());
        return ResponseEntity.ok(ApiResponse.success("Đã thêm biển số xe!", null));
    }

    @GetMapping("/listLicensePlate")
    @Operation(summary = "Lấy danh sách biển số xe của user (Cần Token)")
    public ResponseEntity<ApiResponse<List<String>>> list() {
        return ResponseEntity.ok(ApiResponse.success(carService.getLicensePlates()));
    }

    @PostMapping("/deleteLicensePlate")
    @Operation(summary = "Xóa một hoặc nhiều biển số xe (Cần Token)")
    public ResponseEntity<ApiResponse<Void>> delete(@Valid @RequestBody DeleteLicensePlateRequest request) {
        carService.deleteLicensePlates(request.getLicensePlates());
        return ResponseEntity.ok(ApiResponse.success("Đã xóa biển số xe!", null));
    }
}
