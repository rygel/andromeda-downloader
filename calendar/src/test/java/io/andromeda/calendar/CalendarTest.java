package io.andromeda.calendar;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarTest extends Assert {
    private static Logger logger = LoggerFactory.getLogger(CalendarTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLanguageFragments() throws Exception {
        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/test.ics");
        io.andromeda.calendar.Calendar cal = new io.andromeda.calendar.Calendar("martin", file);
        cal.updateFromFile();
        for (Map.Entry<Long, CalendarItem> entry: cal.getEntries().entrySet()) {
            //LOGGER.info(entry.getKey() + ", " + entry.getValue().toString());
        }
        //assertThat(expected, equalTo(result));
    }

    @Test @SuppressWarnings("unchecked")
    public void testCalendarFileNotFound() throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        final Appender mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

        String currentPath = System.getProperty("user.dir");
        File file = new File(currentPath + "/file_not_found.ics");
        io.andromeda.calendar.Calendar cal = new io.andromeda.calendar.Calendar("test", file);
        cal.updateFromFile();

        verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                return ((LoggingEvent)argument).getFormattedMessage().contains("java.io.FileNotFoundException:");
            }
        }));
    }

}
