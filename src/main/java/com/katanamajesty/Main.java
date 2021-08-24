package com.katanamajesty;

import com.katanamajesty.api.GWApi;
import com.katanamajesty.modules.Config;
import com.katanamajesty.modules.Database;
import com.katanamajesty.modules.Discord;

public class Main {

    public static final String TABLE_NAME = "gw2bot_linked";

    public static void main(String[] args) {
        GWApi.init();
        Config config = new Config();
        config.init();
        Database.initTables();
        Discord discord = new Discord(Config.getString("token"));
        discord.init();

    }

}

