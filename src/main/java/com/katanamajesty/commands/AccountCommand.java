package com.katanamajesty.commands;

import com.katanamajesty.api.v2.account.Account;
import com.katanamajesty.exceptions.NoneAccessAccountException;
import com.katanamajesty.modules.GWLinker;
import com.katanamajesty.modules.format.GWEmoji;
import com.katanamajesty.types.ClickableButton;
import com.katanamajesty.CommandManager;
import com.katanamajesty.types.SlashCommand;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.time.Instant;
import java.util.List;

public class AccountCommand implements SlashCommand, ClickableButton {
    @Override
    public void performButtonReaction(ButtonClickEvent event) {
        // TODO: 23.08.2021
    }

    @Override
    public void performCommand(SlashCommandEvent event) {
        Member member;
        List<OptionMapping> options = event.getOptions();
        member = options.get(0).getAsMember();
        assert member != null;
        if (!GWLinker.isLinked(member)) {
            event.reply("This account doesn't seem to have linked GW2 account. It's owner might not have used `/link` command.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        String gw2Token = GWLinker.getGWToken(member);
        Account account = new Account(gw2Token);
        if (options.size() != 2) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Account statistics");
            builder.setTitle(String.format("Player: *%s*", account.getName()));
            builder.setDescription(String.format("*GUID*: `%s`", account.getGUID()));
            builder.addField("World", account.getWorld(), true);
            builder.addField("Total In-game Time", account.getAge(), true);
            builder.addField("Creation Date", account.getCreatedTimestamp(), true);
            builder.addField("Game Access", getAccessString(account.getAccess()), true);
            builder.addField("Guilds", account.getGuilds(), true);
            builder.addField("Fractal Level", GWEmoji.addEmojiToString(
                    GWEmoji.FRACTAL_RELIC, account.getFractalLevel() + " Fractal Level"
            ), true);
            builder.addField("Daily Achievement Points", GWEmoji.addEmojiToString(
                    GWEmoji.ACHIEVEMENT_POINT,
                    account.getMonthlyAchievementPoints() + " AP"), true);
            builder.addField("Monthly Achievement Points",
                    GWEmoji.addEmojiToString(
                            GWEmoji.ACHIEVEMENT_POINT,
                            account.getMonthlyAchievementPoints() + " AP"), true);
            builder.addField("WVW Rank", getWVWString(account.getWVWRank()), true);
            builder.setFooter(member.getUser().getAsTag(), member.getUser().getAvatarUrl());
            builder.setTimestamp(Instant.now());
            builder.setThumbnail("https://cdn.discordapp.com/attachments/879782344905596939/879782379433099274/gw2_logo.png");
            event.replyEmbeds(builder.build()).queue();
            return;
        }
        OptionMapping mapping = options.get(1);
        CommandManager.AccountArgs arg = CommandManager.AccountArgs.getAccountArg(mapping.getAsString());
        if (arg == null) {
            event.reply("Incorrect argument.").setEphemeral(true).queue();
            return;
        }
        switch (arg) {
            case ACHIEVEMENTS -> event.reply("/v2/account/achievements").queue();
            case BANK -> event.reply("/v2/account/bank").queue();
            case BUILDSTORAGE -> event.reply("/v2/account/buildstorage").queue();
            case DAILYCRAFTING -> event.reply("/v2/account/dailycrafting").queue();
            case DUNGEONS -> event.reply("/v2/account/dungeons").queue();
            case DYES -> event.reply("/v2/account/dyes").queue();
        }
    }

    /*
    ==============================
    МЕТОДЫ ДЛЯ КОМАНДЫ ИДУТ НИЖЕ
    ==============================
     */

    /**
     * :emoji: Tier wvwRank Avenger
     * @param wvwRank wvwRank
     * @return :emoji: Tier wvwRank Avenger lol
     */
    private static String getWVWString(String wvwRank) {
        int rank = Integer.parseInt(wvwRank);
        wvwRank = String.format("Tier %s Avenger", wvwRank);
        switch (rank) {
            case 1 -> wvwRank = GWEmoji.getEmojiFromLong(GWEmoji.WVW_TIER_1_AVENGER) + wvwRank;
            case 2 -> wvwRank = GWEmoji.getEmojiFromLong(GWEmoji.WVW_TIER_2_AVENGER) + wvwRank;
            case 3 -> wvwRank = GWEmoji.getEmojiFromLong(GWEmoji.WVW_TIER_3_AVENGER) + wvwRank;
            case 4 -> wvwRank = GWEmoji.getEmojiFromLong(GWEmoji.WVW_TIER_4_AVENGER) + wvwRank;
            case 5 -> wvwRank = GWEmoji.getEmojiFromLong(GWEmoji.WVW_TIER_5_AVENGER) + wvwRank;
        }
        return wvwRank;
    }

    @SneakyThrows(NoneAccessAccountException.class)
    private static String getAccessString(String access) {
        String result = "Trial Version of GW2";
        if (access.contains("None")) throw new NoneAccessAccountException();
        if (access.contains("GuildWars2")) result = "Full Version of GW2";
        if (access.contains("HeartOfThorns")) result = result + "\nDLC: *Heart Of Thorns*";
        if (access.contains("PathOfFire")) result = result + "\nDLC: *Path Of Fire*";
        if (access.contains("EndOfDragons")) result = result + "\nDLC: *End Of Dragons*";
        return result;
    }
}
