package com.example.obstaclecoursegame.Models;

public class Player {
    private String name = "";
    private int score = 0;
    private double latitude = 0;
    private double longitude = 0;

    public Player() { }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Player setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Player setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
