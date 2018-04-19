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

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CalendarItem {
    private static final ZoneId utcZone = ZoneId.of("Europe/Berlin");
    private Calendar calendar;
    private String name;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private ZonedDateTime lastModified;
    private String location;
    private String description;

    public CalendarItem(Calendar calendar, String name, ZonedDateTime startDateTime, ZonedDateTime endDateTime,
                        ZonedDateTime lastModified, String location, String description) {
        this.calendar = calendar;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.lastModified = lastModified;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + calendar.getName() + "] " + name + "; "
                + startDateTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + endDateTime.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter) + "; "
                + location + "; " + description + "; "
                + lastModified.withZoneSameInstant(utcZone).format(Calendar.outputDateTimeFormatter);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
