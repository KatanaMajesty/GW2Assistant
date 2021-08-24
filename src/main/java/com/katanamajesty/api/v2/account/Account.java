package com.katanamajesty.api.v2.account;

import com.katanamajesty.api.GWApi;
import com.katanamajesty.modules.format.GWEmoji;
import com.katanamajesty.modules.format.GWFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.util.List;

public class Account {

    private final JSONObject json;

    public Account(String gw2Token) {
        json = GWApi.getJSON(GWApi.V1_URLS.get(0), gw2Token);
    }

    /**
     * @return The unique persistent account GUID.
     */
    public String getGUID() {
        return GWApi.getStringFromJson(json, "id");
    }

    /**
     * @return The age of the account as a stamp.
     */
    public String getAge() {
        Long l = GWApi.getLongFromJSON(json, "age");
        assert l != null;
        return GWFormatter.secondsToStamp(l);
    }


    public String getName() {
        return GWApi.getStringFromJson(json, "name");
    }

    /**
     * @return String of home world the account is assigned to. Can be resolved against /v2/worlds
     */
    public String getWorld() {
        Integer i = GWApi.getIntFromJSON(json, "world");
        assert i != null;
        return GWFormatter.getWorldById(i);
    }

    /**
     * @return A dd:MM:yyyy hh:mm timestamp of when the account was created.
     */
    public String getCreatedTimestamp() {
        return GWFormatter.iso8601ToDate(GWApi.getStringFromJson(json, "created"));
    }

    public String getAccess() {
        JSONArray j = GWApi.getJSONArrayFromJson(json, "access");
        assert j != null;
        List<Object> list = j.toList();
        StringBuilder r = new StringBuilder();
        for (Object o : list)
            r.append(o).append("\n");
        return r.toString();
    }

    /*
    GUILDS SCOPE
     */
    /**
     * @return A list of guilds assigned to the given account.
     */
    public String getGuilds() {
        JSONArray obj = GWApi.getJSONArrayFromJson(json, "guilds");
        List<Object> leadGuilds = getLeadGuilds();
        if (noPermissionsInAPIKey(obj) || leadGuilds == null) return "no permissions";
        List<Object> guilds = obj.toList();
        if (guilds.isEmpty()) return "no guilds";
        StringBuilder sb = new StringBuilder();
        for (Object o : guilds) {
            String gName = GWFormatter.getGuildNameById(o.toString());
            if (leadGuilds.contains(o)) sb.append(String.format("%s **%s**", GWEmoji.CROWN, gName)).append("\n");
            else sb.append(gName).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * @return A list of guilds the account is leader of. Requires the additional guilds scope.
     *         NULL если недостаточно прав
     */
    @Nullable
    public List<Object> getLeadGuilds() {
        JSONArray obj = GWApi.getJSONArrayFromJson(json, "guild_leader");
        if (noPermissionsInAPIKey(obj)) return null;
        return obj.toList();
    }

    /*
    PROGRESSION SCOPE
     */
    public String getFractalLevel() {
        Integer i = GWApi.getIntFromJSON(json, "fractal_level");
        if (i == null) return "no permissions";
        return String.valueOf(i);
    }

    public String getDailyAchievementPoints() {
        Integer i = GWApi.getIntFromJSON(json, "daily_ap");
        if (i == null) return "no permissions";
        return String.valueOf(i);
    }

    public String getMonthlyAchievementPoints() {
        Integer i = GWApi.getIntFromJSON(json, "monthly_ap");
        if (i == null) return "no permissions";
        return String.valueOf(i);
    }

    public String getWVWRank() {
        Integer i = GWApi.getIntFromJSON(json, "wvw_rank");
        if (i == null) return "no permissions";
        return String.valueOf(i);
    }

    /**
     * Не используется вообще из-за своего неудобства
     * @return последнее время обновления информации в апишке
     */
    @Deprecated
    public String lastAPIModifiedMoment() {
        return GWApi.getStringFromJson(json, "last_modified");
    }

    private static boolean noPermissionsInAPIKey(Object o) {
        return o == null;
    }

    /*
    fractal_level (number) – The account's personal fractal reward level. Requires the additional progression scope.
    daily_ap (number) – The daily AP the account has. Requires the additional progression scope.
    monthly_ap (number) – The monthly AP the account has. Requires the additional progression scope.
    wvw_rank (number) – The account's personal wvw rank. Requires the additional progression scope.

    API MODIFIED
    last_modified (string) – An ISO-8601 standard timestamp of when the account information last changed as perceived by the API.
    This field is only present when a Schema version of 2019-02-21T00:00:00Z or later is requested.
     */

}
