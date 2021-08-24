package com.katanamajesty;

import com.katanamajesty.commands.AccountCommand;
import com.katanamajesty.commands.LinkDiscordCommand;
import com.katanamajesty.commands.UnlinkDiscordCommand;
import com.katanamajesty.modules.Discord;
import com.katanamajesty.types.ClickableButton;
import com.katanamajesty.types.SlashCommand;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager extends ListenerAdapter {

    private final ConcurrentHashMap<String, SlashCommand> commandConcurrentMap;
    private final ConcurrentHashMap<String, ClickableButton> buttonConcurrentMap;

    public enum AccountArgs {
        MAIN, ACHIEVEMENTS, BANK, BUILDSTORAGE,
        DAILYCRAFTING, DUNGEONS, DYES,
        EMOTES, FINISHERS, GLIDERS,
        HOME;

        @Nullable
        public static AccountArgs getAccountArg(String arg) {
            for (AccountArgs a : AccountArgs.values())
                if (a.name().toLowerCase().equals(arg)) return a;
            return null;
        }
    }

    public CommandManager() {
        /*
        ========================
        КОММАНДЫ
        ========================
         */

        commandConcurrentMap = new ConcurrentHashMap<>();
        commandConcurrentMap.put("link", new LinkDiscordCommand());
        commandConcurrentMap.put("unlink", new UnlinkDiscordCommand());
        commandConcurrentMap.put("account", new AccountCommand());

        CommandListUpdateAction updateAction = Discord.getJda().updateCommands();
        CommandData link = new CommandData("link", "Link GW2 account to Discord")
                .addOptions(new OptionData(OptionType.STRING, "api-key", "get on https://account.arena.net/applications").setRequired(true));
        CommandData unlink = new CommandData("unlink", "Unlink GW2 account from Discord");
        CommandData _account = new CommandData("account", "Retrieve all public information about the account")
                .addOptions(new OptionData(OptionType.USER, "member", "Get information about this player").setRequired(true),
                            new OptionData(OptionType.STRING, "argument", "Additional account information"));
        updateAction.addCommands(link, unlink, _account).queue();

        /*
        ========================
        КНОПКИ
        ========================
         */

        buttonConcurrentMap = new ConcurrentHashMap<>();
        buttonConcurrentMap.put("link_button", new LinkDiscordCommand());
        buttonConcurrentMap.put("unlink_button", new UnlinkDiscordCommand());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        String commandName = event.getName();
        SlashCommand command;
        if ((command = commandConcurrentMap.get(commandName)) != null) {
            command.performCommand(event);
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String buttonID = event.getComponentId();
        ClickableButton object;
        System.out.println(buttonConcurrentMap.get(buttonID));
        if ((object = buttonConcurrentMap.get(buttonID)) != null) {
            object.performButtonReaction(event);
        }
    }
}
