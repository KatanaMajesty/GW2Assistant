package com.katanamajesty.api;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GWApi {

    // v2 urls
    /*
    v2 urls требуют токен
     */
    static String uAccountUrl = "https://api.guildwars2.com/v2/account";
    static String uBankUrl = "https://api.guildwars2.com/v2/account/bank";
    static String uMaterialsUrl = "https://api.guildwars2.com/v2/account/materials";
    static String uCharUrl = "https://api.guildwars2.com/v2/characters";
    static String uTransactionsUrl = "https://api.guildwars2.com/v2/commerce/transactions";
    static String uTokenInfoUrl = "https://api.guildwars2.com/v2/tokeninfo";

    // v1 urls
    static String guildDetailsUrl = "https://api.guildwars2.com/v1/guild_details";
    static String wvwMatchesUrl = "https://api.guildwars2.com/v1/wvw/matches";
    static String wvwMatchDetailsUrl = "https://api.guildwars2.com/v1/wvw/match_details";
    static String wvwObjectiveNamesUrl = "https://api.guildwars2.com/v1/wvw/objective_names";

    // v2 unused for now
    static String itemsUrl = "https://api.guildwars2.com/v2/items";
    static String materialsUrl = "https://api.guildwars2.com/v2/materials";
    static String recipesUrl = "https://api.guildwars2.com/v2/recipes";
    static String recipesSearchUrl = "https://api.guildwars2.com/v2/recipes/search";
    static String skinsUrl = "https://api.guildwars2.com/v2/skins";
    // Controls for this endpoint are still under construction.
    static String continentsUrl = "https://api.guildwars2.com/v2/continents?";
    static String mapsUrl = "https://api.guildwars2.com/v2/maps";
    /*
    Unused urls.
    https://api.guildwars2.com/v2/commerce/listings
    https://api.guildwars2.com/v2/commerce/exchange
    https://api.guildwars2.com/v2/commerce/prices
    https://api.guildwars2.com/v2/build
    https://api.guildwars2.com/v2/colors
    https://api.guildwars2.com/v2/files?
    https://api.guildwars2.com/v2/quaggans
    https://api.guildwars2.com/v2/worlds
     */

    public static final List<String> V1_URLS = new ArrayList<>(6);

    /*
    ========================
    STATIC
    ========================
     */

    /**
     *
     * @param url ссылка на апи без токена пользователя.
     * @return возвращает JSONObject - массив
     */
    @SneakyThrows({MalformedURLException.class, IOException.class})
    public static JSONObject getJSON(String url, String gw2Token) {
        url = urlTokenAdder(url, gw2Token);
        assert url != null;
        URL gwUrl = new URL(url);
        URLConnection gwCon = gwUrl.openConnection();
        StringBuilder result;
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(gwCon.getInputStream()));) {
            result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
        }
        return new JSONObject(result.toString());
    }

    /**
     *
     * @param url ссылка на апи без токена пользователя.
     * @return возвращает JSONObject - массив
     */
    @SneakyThrows({MalformedURLException.class, IOException.class})
    public static JSONObject getJSON(String url) {
        assert url != null;
        URL gwUrl = new URL(url);
        URLConnection gwCon = gwUrl.openConnection();
        StringBuilder result;
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(gwCon.getInputStream()))) {
            result = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
        }
        return new JSONObject(result.toString());
    }

    @SneakyThrows({MalformedURLException.class, IOException.class})
    public static JSONArray getJSONArray(String url) {
        assert url != null;
        URL gwUrl = new URL(url);
        URLConnection gwCon = gwUrl.openConnection();
        StringBuilder result;
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(gwCon.getInputStream()))) {
            result = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
        }
        return new JSONArray(result.toString());
    }


    public static Object getObjFromJSON(JSONObject json, String responseParameter) {
        return json.get(responseParameter);
    }

    @SneakyThrows(NumberFormatException.class)
    public static Integer getIntFromJSON(JSONObject json, String responseParameter) {
        try {
            return Integer.parseInt(json.get(responseParameter).toString());
        } catch (JSONException e) {
            return null;
        }
    }

    @Nullable
    @SneakyThrows(NumberFormatException.class)
    public static Long getLongFromJSON(JSONObject json, String responseParameter) {
        try {
            return Long.parseLong(json.get(responseParameter).toString());
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * @return JSON Array, но NULL, если API-Token не отметил галочку
     */
    @Nullable
    public static JSONArray getJSONArrayFromJson(JSONObject json, String responseParameter) {
        try {
            return json.getJSONArray(responseParameter);
        } catch (JSONException e) {
            return null;
        }
    }

    public static String getStringFromJson(JSONObject json, String responseParameter) {
        return json.get(responseParameter).toString();
    }

    public static boolean getBooleanFromJson(JSONObject json, String responseParameter) {
        return Boolean.getBoolean(json.get(responseParameter).toString());
    }

    private static String urlTokenAdder(String url, String gw2Token) {
        if (V1_URLS.contains(url)) return url.concat("?access_token=" + gw2Token);
        return url;
    }

    public static void init() {
        V1_URLS.add(uAccountUrl);
        V1_URLS.add(uBankUrl);
        V1_URLS.add(uMaterialsUrl);
        V1_URLS.add(uCharUrl);
        V1_URLS.add(uTransactionsUrl);
        V1_URLS.add(uTokenInfoUrl);
    }

}
