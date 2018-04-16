package io.andromeda.calendar;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CalendarItem {
    private static final ZoneId utcZone = ZoneId.of("Europe/Berlin");
    private Calendar calendar;
    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime lastModified;
    private String location;
    private String description;

    public CalendarItem(Calendar calendar, String name, ZonedDateTime startTime, ZonedDateTime endTime,
                        ZonedDateTime lastModified, String location, String description) {
        this.calendar = calendar;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastModified = lastModified;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + calendar.getName() + "] " + name + "; "
                + startTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + endTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + location + "; " + description + "; "
                + lastModified.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter);
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getStartDateTime() {
        return startTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endTime;
    }
}
