package com.katanamajesty.modules;

import com.katanamajesty.CommandManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

public class Discord {

    private static JDA jda;
    private final String TOKEN;
    /**
     * NULL Если Discord.init() не был вызван раньше
     */
    @Nullable
    public static Guild PARENT_GUILD = null;

    public Discord(String token) {
        this.TOKEN = token;
    }

    @SneakyThrows({LoginException.class, InterruptedException.class})
    public void init() {
        jda = JDABuilder.createDefault(TOKEN).setActivity(Activity.playing("GW2")).build();
        jda.awaitReady();
        jda.addEventListener(new CommandManager());
        PARENT_GUILD = jda.getGuildById("879409821315653683");
    }

    public static JDA getJda() {
        assert jda != null;
        return jda;
    }
}
