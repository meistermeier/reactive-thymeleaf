package com.meistermeier.reactive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class Movie {

    private String title;
    @JsonProperty("episode_id")
    private Integer episodeId;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    private String director;
    @JsonProperty("release_date")
    private Date releaseDate;
    private String url;
    private String created;
    private String edited;

    private List<String> species;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> characters;
    private List<String> planets;

    /**
     * Resolves the vehicles in a synchronous way against the API (RestTemplate) and returns them.
     * There is no way (afaik) to get this data also as chunks in the response.
     * Therefore it is a good place to do some loading for each movie element instead of faking a delay.
     *
     * @return the list of vehicles appearing in the movie
     */
    public List<Vehicle> getResolvedVehicles() {
        List<String> vehicleNames = getVehicles();
        if (vehicleNames == null) {
            return Collections.emptyList();
        }

        List<Vehicle> resolvedVehicles = new ArrayList<>();

        // Somehow the api has to get an user agent value for this endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "something like curl");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        RestTemplate client = new RestTemplate();

        for (String vehicleUrl : vehicleNames) {
            resolvedVehicles.add(client.exchange(vehicleUrl, HttpMethod.GET, entity, Vehicle.class).getBody());

        }
        return resolvedVehicles;
    }

}
