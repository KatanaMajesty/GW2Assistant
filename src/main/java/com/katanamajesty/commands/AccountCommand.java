package com.katanamajesty.commands;

import com.katanamajesty.api.v2.account.Account;
import com.katanamajesty.modules.GWLinker;
import com.katanamajesty.types.ClickableButton;
import com.katanamajesty.CommandManager;
import com.katanamajesty.types.SlashCommand;
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
            builder.addField("Player", account.getName(), true);
            builder.addField("GUID", account.getGUID(), true);
            builder.addField("World", account.getWorld(), true);
            builder.addField("Total In-game Time", account.getAge(), true);
            builder.addField("Creation Date", account.getCreatedTimestamp(), true);
            builder.addField("Game Access", account.getAccess(), true);
            builder.addField("Guilds", account.getGuilds(), true);
            builder.addField("Fractal Level", account.getFractalLevel(), true);
            builder.addField("Daily Achievement Points", account.getDailyAchievementPoints(), true);
            builder.addField("Monthly Achievement Points", account.getMonthlyAchievementPoints(), true);
            builder.addField("WVW Rank", account.getWVWRank(), true);
            builder.setTitle("Account statistics");
            builder.setFooter(member.getUser().getAsTag(), member.getUser().getAvatarUrl());
            builder.setTimestamp(Instant.now());
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

}
