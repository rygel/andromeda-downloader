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
