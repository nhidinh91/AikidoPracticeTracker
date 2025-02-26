package app.aikidopracticetracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class PracticeSession {
    private LocalDate date;
    private int duration; // in minutes

    public PracticeSession(LocalDate date, int duration) {
        this.date = date;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " - " + duration + " mins";
    }
}