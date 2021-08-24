package com.katanamajesty.modules.format;

import com.katanamajesty.api.GWApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GWFormatter {

    public static String secondsToStamp(long seconds) {
        long h = 0, m = 0, s;
        s = seconds;
        while (s > 3600) {
            h++;
            s -= 3599;
        }
        while (s > 59) {
            m++;
            s -= 60;
        }
        return String.format("%d hours %d minutes", h, m);
//        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /**
     * dd.MM.yyyy hh:mm
     */
    public static String iso8601ToDate(String iso8601Date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        Date date = Date.from(Instant.from(dateTimeFormatter.parse(iso8601Date)));
        String dateFormat = "dd.MM.yyyy hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static String getWorldById(int id) {
        JSONArray json = GWApi.getJSONArray("https://api.guildwars2.com/v2/worlds?ids=" + id);
        return GWApi.getStringFromJson(json.getJSONObject(0), "name");
    }

    public static String getGuildNameById(String id) {
        JSONObject json = GWApi.getJSON("https://api.guildwars2.com/v2/guild/" + id);
        return GWApi.getStringFromJson(json, "name");
    }

}
