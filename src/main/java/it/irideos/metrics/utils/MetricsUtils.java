package it.irideos.metrics.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MetricsUtils {
  // used for format and return yesterday datetime
  public static String formatterInstantYesterdayToString() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.from(ZoneOffset.UTC));
    Instant now = Instant.now();
    Instant yesterday = now.minus(1, ChronoUnit.DAYS);
    String formattedInstantToString = formatter.format(yesterday);
    String dt = formattedInstantToString.substring(0, 11);
    String hr = "00:00:00";
    String yesterdayDtTime = dt + "" + hr;
    return yesterdayDtTime;
  }

  // used for format and return instant datetime
  public static String formatterInstantNowToString() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.from(ZoneOffset.UTC));
    Instant now = Instant.now();
    String formattedInstantNowToString = formatter.format(now);
    String dtNow = formattedInstantNowToString.substring(0, 11);
    String hr = "00:00:00";
    String nowDtTime = dtNow + "" + hr;
    return nowDtTime;
  }

  public static Timestamp formatterInstantYesterdayToTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.from(ZoneOffset.UTC));
    Instant now = Instant.now();
    Instant yesterday = now.minus(1, ChronoUnit.DAYS);
    String formattedInstantToString = formatter.format(yesterday);
    String dt = formattedInstantToString.substring(0, 11);
    String hr = "00:00:00";
    String yesterdayDtTime = dt + "" + hr;
    LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(yesterdayDtTime));
    Timestamp dtFrom = Timestamp.valueOf(localDateTime);

    return dtFrom;
  }

  public static Timestamp formatterInstantNowToTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.from(ZoneOffset.UTC));
    Instant now = Instant.now();
    String formattedInstantNowToString = formatter.format(now);
    String dtNow = formattedInstantNowToString.substring(0, 11);
    String hr = "00:00:00";
    String nowDtTime = dtNow + "" + hr;
    LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(nowDtTime));
    Timestamp dtFrom = Timestamp.valueOf(localDateTime);

    return dtFrom;
  }
}
