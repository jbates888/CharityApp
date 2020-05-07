package com.example.charityapp;

/**
 * @description Class creates a donor object that can be added to database when a new donor is created
 *
 * @authors Aj Thut
 * @date_created 2/17/20
 * @date_modified 3/26/20
 */
public class Donor {

    //stats for the donor
    private String name;
    private int hours;
    private int donated;

    public Donor(){}

    //donor constructor
    public Donor(String name, int hours, int donate){
        this.name = name;
        this.hours = hours;
        this.donated = donate;
    }

    //donor getters and setters
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

    public void setDonated(int amount){
        this.donated = amount;
    }

    public int getDonated(){ return this.donated;}
}
