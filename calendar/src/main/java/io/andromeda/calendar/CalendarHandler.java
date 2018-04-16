package io.andromeda.calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class CalendarHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Calendar.class);
    private SortedMap<String, Calendar> calendars = new TreeMap<>();

//    public CalendarHandler(Downloader downloader) {
//
//    }

    public void addCalendar(File file, Locale locale) {
        Calendar calendar = new Calendar(file.getName(), file, locale);
        calendar.updateFromFile();
        calendars.put(calendar.getName(), calendar);
    }

}
