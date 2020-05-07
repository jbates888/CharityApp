package com.example.charityapp;

/**
 * @description Class used for creating volunteer in database
 *
 * @author AJ Thut
 * @date_created 02/13/20
 * @date_modified 05/01/20
 */
public class Volunteer {
    //volunteer data
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
