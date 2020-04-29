package com.example.charityapp;

public class Donor {

    private String name;
    private int hours;
    private int donated;

    public Donor(){}

    public Donor(String name, int hours, int donate){
        this.name = name;
        this.hours = hours;
        this.donated = donate;
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

    public void setDonated(int amount){
        this.donated = amount;
    }

    public int getDonated(){ return this.donated;}
}
