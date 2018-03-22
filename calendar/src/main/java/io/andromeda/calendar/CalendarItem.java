package io.andromeda.calendar;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.TextStyle.FULL;

public class CalendarItem {
    private static final ZoneId utcZone = ZoneId.of("Europe/Berlin");
    private static final DateTimeFormatter defaultDay = DateTimeFormatter.ofPattern("dd.MM.YY");
    private static final DateTimeFormatter defaultTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Calendar calendar;
    private String name;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private ZonedDateTime lastModifiedDateTime;
    private String location;
    private String description;

    public CalendarItem(Calendar calendar, String name, ZonedDateTime startDateTime, ZonedDateTime endDateTime,
                        ZonedDateTime lastModifiedDateTime, String location, String description) {
        this.calendar = calendar;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public String getStartDate() {
        return startDateTime.format(defaultDay);
    }

    public String getStartTime() {
        return startDateTime.format(defaultTime);
    }

    public String getStartDay() {
        return startDateTime.getDayOfWeek().getDisplayName(FULL, Locale.GERMAN);
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getEndDate() {
        return endDateTime.format(defaultDay);
    }

    public String getEndTime() {
        return endDateTime.format(defaultTime);
    }

    public String getEndDay() {
        return endDateTime.getDayOfWeek().getDisplayName(FULL, Locale.GERMAN);
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + calendar.getName() + "] " + name + "; "
                + startDateTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + endDateTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + location + "; " + description + "; "
                + lastModifiedDateTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter);
    }
}
