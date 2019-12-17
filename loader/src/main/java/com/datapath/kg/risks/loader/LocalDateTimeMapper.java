package com.datapath.kg.risks.loader;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeMapper {

    public String asString(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public LocalDateTime asDate(String date) {
        return date != null ? ZonedDateTime.parse(date).toLocalDateTime() : null;
    }

}
