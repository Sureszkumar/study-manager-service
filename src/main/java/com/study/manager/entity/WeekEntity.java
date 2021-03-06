package com.study.manager.entity;

import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Embeddable
public class WeekEntity {

    private int monday;

    private int tuesday;

    private int wednesday;

    private int thursday;

    private int friday;

    private int saturday;

    private int sunday;

    public WeekEntity() {
    }

    public WeekEntity(int defaultPages) {
        this(defaultPages, defaultPages, defaultPages, defaultPages, defaultPages, defaultPages, defaultPages);
    }

    public WeekEntity(int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    public int getTotalWeekCount() {
        return this.monday + this.tuesday + this.wednesday + this.thursday + this.friday + this.saturday + this.sunday;
    }

    public int getTotalNonZeroDays() {
        int total = 0;
        if (this.monday > 0) {
            total++;
        }
        if (this.tuesday > 0) {
            total++;
        }
        if (this.wednesday > 0) {
            total++;
        }
        if (this.thursday > 0) {
            total++;
        }
        if (this.friday > 0) {
            total++;
        }
        if (this.saturday > 0) {
            total++;
        }
        if (this.sunday > 0) {
            total++;
        }
        return total;
    }

    public DayOfWeek getNonZeroDay() {
        if (this.monday > 0) {
            return DayOfWeek.MONDAY;
        } else if (this.tuesday > 0) {
            return DayOfWeek.TUESDAY;
        } else if (this.wednesday > 0) {
            return DayOfWeek.WEDNESDAY;
        } else if (this.thursday > 0) {
            return DayOfWeek.THURSDAY;
        } else if (this.friday > 0) {
            return DayOfWeek.FRIDAY;
        } else if (this.saturday > 0) {
            return DayOfWeek.SATURDAY;
        } else {
            return DayOfWeek.SUNDAY;
        }
    }

    public int getTodayGoal(LocalDate now){
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        if(dayOfWeek.equals(DayOfWeek.MONDAY)){
            return this.monday;
        } else if(dayOfWeek.equals(DayOfWeek.TUESDAY)){
            return this.tuesday;
        } else if(dayOfWeek.equals(DayOfWeek.WEDNESDAY)){
            return this.wednesday;
        } else if(dayOfWeek.equals(DayOfWeek.THURSDAY)){
            return this.thursday;
        } else if(dayOfWeek.equals(DayOfWeek.FRIDAY)){
            return this.friday;
        } else if(dayOfWeek.equals(DayOfWeek.SATURDAY)){
            return this.saturday;
        } else {
            return this.sunday;
        }
    }

}
