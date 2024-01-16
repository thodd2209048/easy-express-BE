package com.example.easyexpressbackend.domain.location;

import com.example.easyexpressbackend.domain.hub.Hub;
import com.example.easyexpressbackend.domain.hub.HubService;
import com.example.easyexpressbackend.domain.location.modal.GeocodeResults;
import com.example.easyexpressbackend.domain.location.modal.Location;
import com.uber.h3core.H3Core;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LocationService {
    private final RestTemplate restTemplate;
    private final H3Core h3;

    @Value("${google.api.key}")
    private String geocodeKey;

    @Value("${h3.resolution}")
    private Integer res;

//    8a41436961affff

    @Autowired
    public LocationService(RestTemplate restTemplate,
                           H3Core h3) {
        this.restTemplate = restTemplate;
        this.h3 = h3;
    }

    @SneakyThrows
    public Location getLocationFromAddress(String stringAddress) {
        String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json";
        String address = URLEncoder.encode(stringAddress, StandardCharsets.UTF_8);
        String url = baseUrl + "?address=" + address + "&key=" + this.geocodeKey;

        GeocodeResults results = restTemplate.getForObject(url, GeocodeResults.class);
        String stringResult = restTemplate.getForObject(url, String.class);
        System.out.println(stringResult);
//        assert !results.getResults().isEmpty();
        return results.getResults().get(0).getGeometry().getViewport().getNortheast();
    }

    public String getCellAddressFromAddress(String address, Integer resolution) {
        Location location = this.getLocationFromAddress(address);
        return h3.latLngToCellAddress(location.getLat(), location.getLng(), resolution);
    }

    public String getCellAddressFromLatLng(Double lat, Double lng, Integer resolution) {
        return h3.latLngToCellAddress(lat, lng, resolution);
    }

}


