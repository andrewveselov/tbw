package com.veselov.andrew.tbw.model;

import java.util.Date;
import java.util.Locale;

public class Workout {
    private String title;
    private String description;
    private String imageURL;
    private int RepeatCount;
    private int AttemptCount;
    private int recordRepeatCount;
    private int recordAttemptCount;
    private Date recordDate;
    private int seconds;
    private boolean favorite;

    public Workout(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.recordRepeatCount = 0;
        this.recordAttemptCount = 1;
        this.recordDate = null;
        this.RepeatCount = 0;
        this.AttemptCount = 1;
        this.seconds = 0;
        this.favorite = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getRecordRepeatCount() {
        return recordRepeatCount;
    }

    public void setRecordRepeatCount(int recordRepeatCount) {
        this.recordRepeatCount = recordRepeatCount;
    }

    public int getRecordAttemptCount() {
        return recordAttemptCount;
    }

    public void setRecordAttemptCount(int recordAttemptCount) {
        this.recordAttemptCount = recordAttemptCount;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public int getRepeatCount() {
        return RepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        RepeatCount = repeatCount;
    }

    public void incRepeatCount() {
        RepeatCount++;
    }

    public void decRepeatCount() {
        RepeatCount--;
    }

    public int getAttemptCount() {
        return AttemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        AttemptCount = attemptCount;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getStopwatch() {
        final int MILLS_IN_SECOND = 3600;
        final int SECONDS_IN_MUNUTE = 60;
        int hours = seconds / MILLS_IN_SECOND;
        int minutes = (seconds % MILLS_IN_SECOND) / SECONDS_IN_MUNUTE;
        int secs = seconds % 60;
        return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, secs);
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


}