package com.katanamajesty.commands;

import com.katanamajesty.modules.GWLinker;
import com.katanamajesty.modules.format.GWEmoji;
import com.katanamajesty.types.ClickableButton;
import com.katanamajesty.types.SlashCommand;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

// TODO: 23.08.2021 переписать класc, заменить assert и getEmoteById()
// TODO: 23.08.2021 создать методы для кнопок, создать класс-конструктор кнопок
public class LinkDiscordCommand extends GWLinker implements SlashCommand, ClickableButton {

    @Override
    public void performCommand(SlashCommandEvent event) {
        Emote emote = GWEmoji.getGW2DragonHeadEmote();
        assert emote != null;
        Member member = event.getMember();
        assert member != null;
        String id = event.getUser().getId();
        if (isLinked(member)) {
            event.reply("This Discord account is already linked to some API-key. Use `/unlink` in order to change it.")
                    .setEphemeral(true)
                    .queue();
            return;
        }
        String apiKey = event.getOptions().get(0).getAsString();

        if (!isGWToken(apiKey)) {
            event.reply(String.format("Argument ||%s|| doesn't seem to be a GW2 API-key, which you can generate here -> https://account.arena.net/applications", apiKey))
                    .setEphemeral(true)
                    .queue();
            return;
        }
        gwApiKeys.put(id, apiKey);
        event.reply(String.format("Link this Discord account to GW2 account with API-key ||%s||?", apiKey))
                .setEphemeral(true)
                .addActionRow(Button.success("link_button", "Link")
                        .withEmoji(Emoji.fromEmote(emote)))
                .queue();
    }

    @Override
    public void performButtonReaction(ButtonClickEvent event) {
        Member member = event.getMember();
        assert member != null;
        String id = member.getUser().getId();
        linkProcess(event, member, id);
    }

}
