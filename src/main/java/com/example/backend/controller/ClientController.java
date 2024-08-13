package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.model.CityVehicleDetail;
import com.example.backend.model.Client;
import com.example.backend.model.Feedback;
import com.example.backend.model.TransportProvider;
import com.example.backend.repository.FeedbackRepository;
import com.example.backend.service.ClientService;
import com.example.backend.service.FeedbackService;
import com.example.backend.service.TransportProviderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
//@PreAuthorize("hasAuthority('ROLE_CLIENT')")
public class ClientController {
   

    @Autowired
    private TransportProviderService providerService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private FeedbackRepository feedbackRepository;
    

     @GetMapping("/findallproviders")
    public List<TransportProvider> getAllProviders() {
        return providerService.getAllProviders();
    }
    @GetMapping("/findprovidersbycities")
    public List<String> getProvidersByCities(@RequestParam String fromCity, @RequestParam String toCity) {
        List<TransportProvider> allProviders = providerService.getAllProviders();
        return allProviders.stream()
                .filter(provider -> provider.getCityVehicleDetails().stream()
                        .anyMatch(detail -> detail.getCity().equals(fromCity)) &&
                        provider.getCityVehicleDetails().stream()
                        .anyMatch(detail -> detail.getCity().equals(toCity)))
                .map(TransportProvider::getLogisticsName)
                .collect(Collectors.toList());
    }
    

    

    @GetMapping("/checkavailability_of_providers")
    public String checkVehicleAvailability(
            @RequestParam String logisticsName,
            @RequestParam String city,
            @RequestParam String vehicleType) {
        return providerService.checkVehicleAvailability(logisticsName, city, vehicleType);
    }
    

    

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/getfeedbacks")
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }
    // @PostMapping("/c")
    // public Client createClient(@RequestBody Client client) {
    //     return clientService.saveClient(client);
    // }

    // @GetMapping
    // public List<Client> getAllClients() {
    //     return clientService.getAllClients();
    // }

    // @GetMapping("/{id}")
    // public Client getClientById(@PathVariable Long id) {
    //     return clientService.getClientById(id);
    // }

    // @PostMapping("/signin")
    // public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest) {
    //     Client client = clientService.authenticateClient(signInRequest.getEmail(), signInRequest.getPassword());
    //     if (client != null) {
    //         return ResponseEntity.ok("Sign in successful");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sign in failed: Invalid email or password");
    //     }
    // }
    
}
