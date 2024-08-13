package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.TransportProvider;
import com.example.backend.model.CityVehicleDetail;
import com.example.backend.repository.TransportProviderRepository;
import com.example.backend.repository.CityVehicleDetailRepository;

@Service
public class TransportProviderService {

    @Autowired
    private TransportProviderRepository transportProviderRepository;

    @Autowired
    private CityVehicleDetailRepository cityVehicleDetailRepository;

    public TransportProvider saveProvider(TransportProvider provider) {
        return transportProviderRepository.save(provider);
    }

    public List<TransportProvider> getAllProviders() {
        return transportProviderRepository.findAll();
    }

    public TransportProvider getProviderById(Long id) {
        return transportProviderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteProvider(Long id) {
        List<CityVehicleDetail> details = cityVehicleDetailRepository.findAll().stream()
                .filter(detail -> detail.getTransportProvider().getId().equals(id))
                .collect(Collectors.toList());

        for (CityVehicleDetail detail : details) {
            cityVehicleDetailRepository.delete(detail);
        }

        transportProviderRepository.deleteById(id);
    }

    public CityVehicleDetail saveCityVehicleDetail(CityVehicleDetail detail) {
        return cityVehicleDetailRepository.save(detail);
    }

    public List<CityVehicleDetail> getCityVehicleDetailsByProvider(Long providerId) {
        return cityVehicleDetailRepository.findAll().stream()
                .filter(detail -> detail.getTransportProvider().getId().equals(providerId))
                .collect(Collectors.toList());
    }

    public String checkVehicleAvailability(String logisticsName, String city, String vehicleType) {
        TransportProvider provider = transportProviderRepository.findByLogisticsName(logisticsName);
        if (provider == null) {
            return "Sorry, The chosen option is currently unavailable";
        }
        
        CityVehicleDetail detail = provider.getCityVehicleDetails().stream()
                .filter(d -> d.getCity().equals(city))
                .findFirst()
                .orElse(null);

        if (detail == null) {
            return "Sorry, The chosen option is currently unavailable";
        }

        boolean available = false;
        switch (vehicleType.toLowerCase()) {
            case "small":
                available = detail.getSmallVehicles() > 0;
                break;
            case "medium":
                available = detail.getMediumVehicles() > 0;
                break;
            case "large":
                available = detail.getLargeVehicles() > 0;
                break;
            default:
                return "Sorry, The chosen option is currently unavailable";
        }

        return available ? "Selection Successful" : "Sorry, The chosen option is currently unavailable";
    }


    public TransportProvider authenticateProvider(String email, String password) {
        TransportProvider provider = transportProviderRepository.findByEmail(email);
        if (provider != null && provider.getPassword().equals(password)) {
            return provider;
        }
        return null;
    }
}
