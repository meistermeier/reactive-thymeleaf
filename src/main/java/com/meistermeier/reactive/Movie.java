package com.meistermeier.reactive;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(Integer episodeId) {
        this.episodeId = episodeId;
    }

    public String getOpeningCrawl() {
        return openingCrawl;
    }

    public void setOpeningCrawl(String openingCrawl) {
        this.openingCrawl = openingCrawl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<String> getStarships() {
        return starships;
    }

    public void setStarships(List<String> starships) {
        this.starships = starships;
    }

    public List<String> getVehicles() {
        return vehicles;
    }

    public List<Vehicle> getResolvedVehicles() {
        List<Vehicle> resolvedVehicles = new ArrayList<>();
        WebClient client = WebClient.create();
        for (String vehicleUrl : getVehicles()) {
            resolvedVehicles.add(client.get().uri(vehicleUrl).accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.bodyToMono(Vehicle.class)).block());
        }
        return resolvedVehicles;
    }

    public List<Vehicle> getResolvedSyncVehicles() throws InterruptedException {
        List<Vehicle> resolvedVehicles = new ArrayList<>();

        // Somehow the api has to get an user agent value for this endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "something like curl");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        RestTemplate client = new RestTemplate();

        for (String vehicleUrl : getVehicles()) {
            resolvedVehicles.add(client.exchange(vehicleUrl, HttpMethod.GET, entity, Vehicle.class).getBody());

        }
        return resolvedVehicles;
    }

    public void setVehicles(List<String> vehicles) {
        this.vehicles = vehicles;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public List<String> getPlanets() {
        return planets;
    }

    public void setPlanets(List<String> planets) {
        this.planets = planets;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", episodeId=" + episodeId +
                ", openingCrawl='" + openingCrawl + '\'' +
                ", director='" + director + '\'' +
                ", releaseDate=" + releaseDate +
                ", url='" + url + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                ", species=" + species +
                ", starships=" + starships +
                ", vehicles=" + vehicles +
                ", characters=" + characters +
                ", planets=" + planets +
                '}';
    }
}
