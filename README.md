# SimpleCalendar

<p align="center">
<img src="https://github.com/lhjnano/SimpleCalendar/blob/main/CalendarTest.PNG?raw=true" width="300" height="600" align="center" />
  <br/>
  <br/>
  <br/>
  <br/>
  <br/>
</p>

## :smiley: What should The Simple Calendar do.   

---
- [X] Events can be displayed on the calendar.

  ```java
  simpleCalendar.setOnEventListener((startTimestamp, endTimestamp) ->
    {
      // Feb
      // +------ ... ------+
      // | 1 | 2 | ... | 7 | // StartTimestamp is '1 Feb' at unixtime
      // | ...             |
      // | ...         |28 |
      // | 1 | ...         |
      // |     ...     |14 | // EndTimestamp is '14 Mar' at unixtime
      // +------ ... ------+
      // TODO return List events from startTimestamp to endTimeStamp
      // DayEvent object can set title, color of background, color of title and time.
      return (List<DayEvent>)list;
    }
  );
  ```
---
- [X] Customize the color of your calendar and events to xml
  <img src="https://github.com/lhjnano/SimpleCalendar/blob/main/colorSample.png?raw=true" width="600" height="500" align="center" />
  
---
- [X] Add a specific day as a holiday.
  ```java
  simpleCalendar.setOnHolidayListener((year, month) -> {
      // New Year Holiday (1/Jan) of Korea.
      if( month == 1 ) return Collections.singletonList(1L);
      // TODO write your holiday format (day list)
      return null;
  });
  ```
---
- [X] Know the date that you clicked in the calendar.
  ```java
  simpleCalendar.setOnDayClickListener(time -> {
    Log.i(MainActivity.class.getSimpleName(), "click on : " + WeekFormat.getDateStringBestFmt(new Date(time), "yyyy MM dd"));

    list.add(new DayEvent("sample", time, time));
    simpleCalendar.notifyDataSetChanged();

    // TODO write what do you do
  });
  ```
---
- [X] Know what month it is whenever you swipe.

  ```java
  simpleCalendar.setOnSwipeMonthListener(time ->
        // TODO When changed the page, write for what to do
        Toast.makeText(MainActivity.this, "swipe on : " + WeekFormat.getDateStringBestFmt(new Date(time), "MM"), Toast.LENGTH_SHORT).show()
  );

  ```
---
- [X] Move a calendar to a specific date.

  ```java
  c.setTimeInMillis(today);
  c.set(Calendar.MONTH, 3); // Maple
  simpleCalendar.setDate(c.getTimeInMillis());
  ```


## Setup

- [X] build.gradle in preject 

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

- [X] build.gradle in app

```
android{
    compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
    }
}

dependencies {
  implementation 'com.github.lhjnano:SimpleCalendar:1.0.1' // Put in recently version code 
}

```

> minSdkVersion is 19, targetSdkVersion is 30 in version 1.0.1


## License

See [LICENSE](https://github.com/lhjnano/SimpleCalendar/blob/main/LICENSE) details.

