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

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarTest extends Assert {
    private static Logger logger = LoggerFactory.getLogger(CalendarTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCalendar() throws Exception {
        String name = "test";
        Locale locale = Locale.ENGLISH;
        String calendarName = "Test Calendar";
        String calendarDescription = "This is a test calendar!";
        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/src/test/resources/calendars/test.ics");
        io.andromeda.calendar.Calendar calendar = new io.andromeda.calendar.Calendar(name, file, locale);
        calendar.updateFromFile();
        for (Map.Entry<Long, CalendarItem> entry: calendar.getEntries().entrySet()) {
            //LOGGER.info(entry.getKey() + ", " + entry.getValue().toString());
        }
        assertThat(name, equalTo(calendar.getName()));
        assertThat(locale, equalTo(calendar.getLocale()));
        assertThat(calendarName, equalTo(calendar.getCalendarName()));
        assertThat(calendarDescription, equalTo(calendar.getDescription()));
        assertThat(2, equalTo(calendar.getEntriesList().size()));
    }

    @Test
    public void testCalendarWithFilter() throws Exception {
        String name = "test";
        Locale locale = Locale.ENGLISH;
        String calendarName = "Test Calendar";
        String calendarDescription = "This is a test calendar!";
        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/src/test/resources/calendars/test.ics");
        io.andromeda.calendar.Calendar calendar = new io.andromeda.calendar.Calendar(name, file, locale);
        calendar.setNameFilter("INM");
        calendar.setStripNameFilter(true);
        calendar.updateFromFile();
        for (Map.Entry<Long, CalendarItem> entry: calendar.getEntries().entrySet()) {
            //LOGGER.info(entry.getKey() + ", " + entry.getValue().toString());
        }
        assertThat(name, equalTo(calendar.getName()));
        assertThat(locale, equalTo(calendar.getLocale()));
        assertThat(calendarName, equalTo(calendar.getCalendarName()));
        assertThat(calendarDescription, equalTo(calendar.getDescription()));
        assertThat(2, equalTo(calendar.getEntriesList().size()));
    }

    @Test @SuppressWarnings("unchecked")
    public void testCalendarFileNotFound() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/src/test/resources/calendars/file_not_found.ics");
        io.andromeda.calendar.Calendar cal = new io.andromeda.calendar.Calendar("test", file, Locale.ENGLISH);
        cal.updateFromFile();

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("java.io.FileNotFoundException:");
            }
        }));
    }

}
