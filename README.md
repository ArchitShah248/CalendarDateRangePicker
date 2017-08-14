# CalendarDateRangePicker
==================

An Android Library to pick dates range, that helps user to select range from future dates.

**Features:**

- Date selection
- Full customization
- Small in size
- Material design support
- Resolution support

Screenshots
-----------

<img width="30%" src="screenshots/device-2017-08-14-143753.png" />
<img width="30%" src="screenshots/device-2017-08-14-143933.png" />


Developer setup
---------------

**Import DateRangePicker dependency**


For Gradle:
compile 'com.archit.calendar:awesome-calendar:1.0'

For Maven:
<dependency>
  <groupId>com.archit.calendar</groupId>
  <artifactId>awesome-calendar</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>


**Usage**
---------------
`<com.archit.calendardaterangepicker.customviews.AwesomeCalendarView
             android:id="@+id/calendar"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             custom:disable_date_color="#ABABAB"
             custom:header_bg="@drawable/calendar_header"
             custom:range_color="@color/range_color"
             custom:selected_date_circle_color="@color/selected_date_circle_color"
             custom:title_color="@color/calendar_header_title">
</com.archit.calendardaterangepicker.customviews.AwesomeCalendarView>`




### Requirements

- Java 8
- Latest version of Android SDK and Android Build Tools


License
-------

    Copyright 2016 Archit Shah

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

