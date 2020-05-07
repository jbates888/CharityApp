package com.example.charityapp;

/**
 * @description this class is for creating event objects
 *
 * @authors Jack Bates
 * @date_created 2/18/20
 * @date_modified 2/29/20
 */

public class Event {
    //declare all event attributes
    private String Name;
    private String Program;
    private String Date;
    private String Time;
    private int Funding;
    private String Description;
    private String Volunteers;
    private int VolunteersNeeded;
    private int NumVolunteers;

    //constructor
    public Event() {

    }

    //constructor with arguments for events
    public Event(String Name, String Program, String Date, String Time, int Funding, String Description, String Volunteers, int VolunteersNeeded, int NumVols) {
        //set all attributes
        this.Name = Name;
        this.Program = Program;
        this.Date = Date;
        this.Time = Time;
        this.Funding = Funding;
        this.Description = Description;
        this.Volunteers = Volunteers;
        this.VolunteersNeeded = VolunteersNeeded;
        this.NumVolunteers = NumVols;
    }

    //getters and setter for all event attributes
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getProgram() {
        return Program;
    }

    public void setProgram(String Program) {
        this.Program = Program;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getFunding() {
        return Funding;
    }

    public void setFunding(int Funding) {
        this.Funding = Funding;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getVolunteers() {
        return Volunteers;
    }

    public void setVolunteers(String Volunteers) {
        this.Volunteers = Volunteers;
    }

    public int getVolunteersNeeded() {
        return VolunteersNeeded;
    }

    public void setVolunteersNeeded(int VolunteersNeeded) {
        this.VolunteersNeeded = VolunteersNeeded;
    }

    public void setNumVolunteers(int num) {
        this.NumVolunteers = num;
    }

    public int getNumVolunteers() {
        return NumVolunteers;
    }
}
