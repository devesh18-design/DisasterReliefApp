package com.relief.model;

public class ReliefCamp {
    private final String campId;
    private final String campName;
    private final String location;
    private final int population;

    public ReliefCamp(String campId, String campName, String location, int population) {
        this.campId = campId;
        this.campName = campName;
        this.location = location;
        this.population = population;
    }

    public String getCampId() { return campId; }
    public String getCampName() { return campName; }
    public String getLocation() { return location; }
    public int getPopulation() { return population; }
}