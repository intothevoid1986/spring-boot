package it.irideos.metrics.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Utils {
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

  public static String formatterInstantNowToString() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME
        .withZone(ZoneId.from(ZoneOffset.UTC));
    Instant now = Instant.now();
    String formattedInstantNowToString = formatter.format(now);
    String dtNow = formattedInstantNowToString.substring(0, 11);
    String hr = "00:00:00";
    String NowDtTime = dtNow + "" + hr;
    return NowDtTime;
  }
}
