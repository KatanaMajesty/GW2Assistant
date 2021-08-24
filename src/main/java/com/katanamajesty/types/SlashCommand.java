package com.katanamajesty.types;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommand {
    void performCommand(SlashCommandEvent event);
}
