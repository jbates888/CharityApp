package com.example.charityapp;

import android.widget.Toast;

public class Event {
    private String Name;
    private String Program;
    private String Date;
    private String Time;
    private int Funding;
    private String Description;
    private String Volunteers;
    private int VolunteersNeeded;

    public Event() {

    }

    public Event(String Name, String Program, String Date, String Time, int Funding, String Description, String Volunteers, int VolunteersNeeded) {
        this.Name = Name;
        this.Program = Program;
        this.Date = Date;
        this.Time = Time;
        this.Funding = Funding;
        this.Description = Description;
        this.Volunteers = Volunteers;
        this.VolunteersNeeded = VolunteersNeeded;
    }

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
}
