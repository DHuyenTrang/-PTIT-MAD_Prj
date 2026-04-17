package com.traffic.traffic_be.service;

import java.util.List;

public interface CarService {
    void addLicensePlate(String licensePlate);
    List<String> getLicensePlates();
    void deleteLicensePlates(List<String> licensePlates);
}
