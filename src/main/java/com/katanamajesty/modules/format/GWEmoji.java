package com.katanamajesty.modules.format;

import com.katanamajesty.modules.Discord;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Emote;

public class GWEmoji {

    /*
    ДЕФОЛТНЫЕ ЭМОДЗИ
     */
    public static final String CROWN = ":crown:";

    /*
    КАСТОМНЫЕ ЭМОДЗИ
     */
    public static final long GW2_DRAGON_HEAD_ID = 879421791976693850L;
    public static final long WVW_TIER_1_AVENGER = 879771842469048331L;
    public static final long WVW_TIER_2_AVENGER = 879771842817179678L;
    public static final long WVW_TIER_3_AVENGER = 879771843400200252L;
    public static final long WVW_TIER_4_AVENGER = 879771842800410654L;
    public static final long WVW_TIER_5_AVENGER = 879771842536161291L;
    public static final long ACHIEVEMENT_POINT = 879774843531317258L;
    public static final long FRACTAL_RELIC = 879776614303555684L;

    public static Emote getGW2DragonHeadEmote() {
        return Discord.getJda().getEmoteById(GW2_DRAGON_HEAD_ID);
    }

    public static String getEmojiFromLong(long l) {
        Emote emote = Discord.getJda().getEmoteById(l);
        if (emote == null) throw new NullPointerException("Emote не найден");
        Emoji e = Emoji.fromEmote(emote);
        return String.format("<:%s:%s>", e.getName(), e.getId());
    }

    public static String addEmojiToString(long l, String s) {
        return getEmojiFromLong(l) + s;
    }

}
