/*
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Main calendar class backed by ical.
 * @author Alexander Brandt
 */
public class Calendar {
    private static final Logger LOGGER = LoggerFactory.getLogger(Calendar.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
    public static final DateTimeFormatter outputDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final Comparator<CalendarItem> startDateTimeComparator =
            Comparator.comparing(CalendarItem::getStartDateTime);

    private File file;
    private String name;
    private String calendarName;
    private Locale locale;
    private String nameFilter = null;
    private boolean stripNameFilter = false;
    private String description;
    private List<CalendarItem> events = new ArrayList<>();
    private SortedMap<Long, CalendarItem> entries = new TreeMap();

    public Calendar(String name, File file, Locale locale) {
        this.name = name;
        this.file = file;
        this.locale = locale;
    }

    private LocalDateTime getLocalDateTime(String string) {
        LocalDateTime result = null;

        // If the event is only specified for the day
        if (string.length() == 8) {
            string = string + "T000001Z";
        }

        try {
            result = LocalDateTime.parse(string, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            LOGGER.error(e.toString());
        }
        return result;
    }

    public boolean updateFromFile() {
        try (FileInputStream fin = new FileInputStream(file)) {
            entries.clear();

            CalendarBuilder builder = new CalendarBuilder();
            TimeZoneRegistry registry = builder.getRegistry();

            net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
            calendarName = calendar.getProperty("X-WR-CALNAME").getValue();
            description = calendar.getProperty("X-WR-CALDESC").getValue();
            String prodid = calendar.getProperty(Property.PRODID).getValue();
            ComponentList<CalendarComponent> components = calendar.getComponents("VEVENT");
            for (CalendarComponent event: components) {
                String eventName = event.getProperty(Property.SUMMARY).getValue().trim();
                if ((nameFilter == null) || (eventName.startsWith(nameFilter))) {
                    if ((stripNameFilter) && (nameFilter != null)) {
                        eventName = eventName.substring(nameFilter.length());
                    }
                    LocalDateTime localDateTime = getLocalDateTime(event.getProperty(Property.DTSTART).getValue());
                    ZonedDateTime startTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                    localDateTime = getLocalDateTime(event.getProperty(Property.DTEND).getValue());
                    ZonedDateTime endTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                    localDateTime = getLocalDateTime(event.getProperty(Property.LAST_MODIFIED).getValue());
                    ZonedDateTime lastModified = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
                    String location = event.getProperty(Property.LOCATION).getValue().trim();
                    String eventDescription = event.getProperty(Property.DESCRIPTION).getValue().trim();
                    CalendarItem calendarItem = new CalendarItem(this, eventName, startTime, endTime, lastModified, location, eventDescription);
                    events.add(calendarItem);
                    entries.put(startTime.toEpochSecond(), calendarItem);
                } else {
                  LOGGER.debug("Event with the name \"{}\" was filtered out due to active name filter \"{}\".", eventName, nameFilter);
                }
            }
            events.sort(startDateTimeComparator);
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

    public String getCalendarName() {
        return calendarName;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public boolean getStripNameFilter() {
        return stripNameFilter;
    }

    public String getDescription() {
        return description;
    }

    public SortedMap<Long, CalendarItem> getEntries() {
        return entries;
    }

    public List<CalendarItem> getEntriesList() {
        return new ArrayList(entries.values());
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public void setStripNameFilter(boolean stripNameFilter) {
        this.stripNameFilter = stripNameFilter;
    }

    public SortedMap<Integer, List<CalendarItem>> getEventsCW() {
        SortedMap<Integer, List<CalendarItem>> result = new TreeMap<>();
        for (CalendarItem event: events) {
            String week = event.getStartDateTime().format(DateTimeFormatter.ofPattern("w", locale));
            int weekInt = Integer.parseInt(week);
            List<CalendarItem> list;
            List<CalendarItem> existingList = result.get(weekInt);

            if (existingList == null) {
                list = new ArrayList<>();
                result.put(weekInt, list);
            } else {
                list = existingList;
            }
            list.add(event);
        }
        return result;
    }

    public Map<String, List<CalendarItem>> getEventsPerName() {
        Map<String, List<CalendarItem>> result = new TreeMap<>();
        for (CalendarItem event: events) {
            //String eventName = "<h2><a href=\"" + event.url + "\">" + event.eventName + "</a></h2>";
            String eventName = event.getName();
            List<CalendarItem> list;
            List<CalendarItem> existingList = result.get(eventName);

            if (existingList == null) {
                list = new ArrayList<>();
                result.put(eventName, list);
            } else {
                list = existingList;
            }
            list.add(event);
        }
        return result;
    }

}
