package io.andromeda.calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

public class CalendarHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Calendar.class);
    private SortedMap<String, Calendar> calendars = new TreeMap<>();

    public void addCalendar(File file) {
        Calendar calendar = new Calendar(file.getName(), file);
        calendar.updateFromFile();
        calendars.put(calendar.getName(), calendar);
    }

}
