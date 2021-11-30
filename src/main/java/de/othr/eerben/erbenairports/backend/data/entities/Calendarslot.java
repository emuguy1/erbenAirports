package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Calendarslot {


    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long calendarId;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private boolean booked;

    @Column(nullable = false)
    private int durationInMinutes;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startTime;


    public Calendarslot(){};

    public Calendarslot(int day, int month, int year, boolean booked, int durationInMinutes, Date startTime) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.booked = booked;
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendarslot that = (Calendarslot) o;
        return calendarId == that.calendarId && day == that.day && month == that.month && year == that.year && booked == that.booked && durationInMinutes == that.durationInMinutes && startTime.equals(that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarId, day, month, year, booked, durationInMinutes, startTime);
    }

    @Override
    public String toString() {
        return "Calendarslot{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", booked=" + booked +
                ", durationInMinutes=" + durationInMinutes +
                ", startTime=" + startTime +
                '}';
    }
}
