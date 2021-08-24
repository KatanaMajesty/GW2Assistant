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
public class UnlinkDiscordCommand extends GWLinker implements SlashCommand, ClickableButton {

    @Override
    public void performCommand(SlashCommandEvent event) {
        Emote emote = GWEmoji.getGW2DragonHeadEmote();
        assert emote != null;
        Member member = event.getMember();
        assert member != null;
        if (!isLinked(member)) {
            event.reply("This Discord account doesn't seem to be associated with any GW2 account.").setEphemeral(true).queue();
            return;
        }
        event.reply(String.format("Unlink this Discord account from GW2 by deleting API-key ||%s|| from bot database?", getGWToken(member)))
                .setEphemeral(true)
                .addActionRow(Button.success("unlink_button", "Unlink")
                        .withEmoji(Emoji.fromEmote(emote)))
                .queue();
    }

    @Override
    public void performButtonReaction(ButtonClickEvent event) {
        Member member = event.getMember();
        assert member != null;
        unlinkProcess(event, member);
    }
}
