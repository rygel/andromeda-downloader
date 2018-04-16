package io.andromeda.calendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.SortedMap;
import java.util.TreeMap;

public class Calendar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Calendar.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
    public static final DateTimeFormatter outputDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private File file;
    private String name;
    private String description;
    private SortedMap<Long, CalendarItem> entries = new TreeMap();

    public Calendar(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public boolean updateFromFile() {
        try (FileInputStream fin = new FileInputStream(file)) {
            entries.clear();

            CalendarBuilder builder = new CalendarBuilder();
            TimeZoneRegistry registry = builder.getRegistry();

            net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
            ComponentList<CalendarComponent> components = calendar.getComponents("VEVENT");
            for (CalendarComponent event: components) {
                String name = event.getProperty(Property.SUMMARY).getValue().trim();
                LocalDateTime localDateTime = LocalDateTime.parse(event.getProperty(Property.DTSTART).getValue(), dateTimeFormatter);
                ZonedDateTime startTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                localDateTime = LocalDateTime.parse(event.getProperty(Property.DTEND).getValue(), dateTimeFormatter);
                ZonedDateTime endTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                localDateTime = LocalDateTime.parse(event.getProperty(Property.LAST_MODIFIED).getValue(), dateTimeFormatter);
                ZonedDateTime lastModified = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                String location = event.getProperty(Property.LOCATION).getValue().trim();
                String description = event.getProperty(Property.DESCRIPTION).getValue().trim();
                CalendarItem calendarItem = new CalendarItem(this, name, startTime, endTime, lastModified, location, description);
                entries.put(startTime.toEpochSecond(), calendarItem);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.toString());
            return false;
        } catch (IOException | ParserException e) {
            LOGGER.error(e.toString());
            return false;
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public SortedMap<Long, CalendarItem> getEntries() {
        return entries;
    }
}
