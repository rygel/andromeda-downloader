package io.andromeda.calendar;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;

public class CalendarItemTest extends Assert {
    private static Logger logger = LoggerFactory.getLogger(CalendarItemTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCalendarItem() throws Exception {
        String name = "test";
        ZonedDateTime start = null;
        ZonedDateTime end = null;
        ZonedDateTime lastModified = null;
        String location = "here";
        String description = "A short description.";
        CalendarItem item = new CalendarItem(null, name, start, end, lastModified, location, description);
        assertThat(null, equalTo(item.getCalendar()));
        assertThat(name, equalTo(item.getName()));
        assertThat(start, equalTo(item.getStartDateTime()));
        assertThat(end, equalTo(item.getEndDateTime()));
        assertThat(lastModified, equalTo(item.getLastModified()));
        assertThat(location, equalTo(item.getLocation()));
        assertThat(description, equalTo(item.getDescription()));
    }
}