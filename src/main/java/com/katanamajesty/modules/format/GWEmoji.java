package com.katanamajesty.modules.format;

import com.katanamajesty.modules.Discord;
import net.dv8tion.jda.api.entities.Emote;

public class GWEmoji {

    /*
    ДЕФОЛТНЫЕ ЭМОДЗИ
     */
    public static final String CROWN = ":crown:";

    /*
    КАСТОМНЫЕ ЭМОДЗИ
     */
    private static final long GW2_DRAGON_HEAD_ID = 879421791976693850L;

    public static Emote getGW2DragonHeadEmote() {
        return Discord.getJda().getEmoteById(GW2_DRAGON_HEAD_ID);
    }

}
