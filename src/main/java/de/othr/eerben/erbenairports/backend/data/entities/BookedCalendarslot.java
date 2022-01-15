package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

//BookedCalendarslot is a slot for a airport that always includes 5 min intervalls

@Entity
public class BookedCalendarslot {


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
    private int durationInMinutes;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startTime;

    @ManyToOne
    private Airport airport;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name="flightdetails_flightid")
    private Flightdetails flightdetails;


    public BookedCalendarslot(){};

    public BookedCalendarslot(int day, int month, int year, int durationInMinutes, Date startTime,Airport airport) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.durationInMinutes = durationInMinutes;
        this.startTime = startTime;
        this.airport = airport;
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

    public Airport getAirport(){ return airport; }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedCalendarslot that = (BookedCalendarslot) o;
        return calendarId == that.calendarId && day == that.day && month == that.month && year == that.year && durationInMinutes == that.durationInMinutes && airport.equals(this.airport) && startTime.equals(that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarId, day, month, year, durationInMinutes, startTime, airport);
    }

    @Override
    public String toString() {
        return "Calendarslot{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", durationInMinutes=" + durationInMinutes +
                ", startTime=" + startTime +
                ", airport=" + airport +
                '}';
    }
}
