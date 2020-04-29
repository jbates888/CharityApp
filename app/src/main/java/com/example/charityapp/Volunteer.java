package com.example.charityapp;

public class Volunteer {

    private String name;
    private int hours;

    public Volunteer(){}

    public Volunteer(String name, int hours){
        this.name = name;
        this.hours = hours;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setHours(int hours){
        this.hours = hours;
    }

    public int getHours(){
        return this.hours;
    }
}
