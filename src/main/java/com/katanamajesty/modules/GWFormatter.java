package com.katanamajesty.modules;

import com.katanamajesty.api.GWApi;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static String getWorldById(int id) {
        JSONArray json = GWApi.getJSONArray("https://api.guildwars2.com/v2/worlds?ids=" + id);
        return GWApi.getStringFromJson(json.getJSONObject(0), "name");
    }

}
