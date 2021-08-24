package com.katanamajesty.types;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface ClickableButton {
    void performButtonReaction(ButtonClickEvent event);
}
