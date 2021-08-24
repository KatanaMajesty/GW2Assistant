package com.katanamajesty.modules;

import com.katanamajesty.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

// TODO: 23.08.2021 сократить код путём создания новых методов для кнопок
public abstract class GWLinker {

    protected static final Map<String, String> gwApiKeys = new HashMap<>();

    /*
    ===================================
    ПРОЦЕССЫ ОТВЯЗКИ/ПРИВЯЗКИ
    ===================================
     */
    protected static void linkProcess(ButtonClickEvent event, Member member, String id) {
        Message message = event.getMessage();
        if (isLinked(member) || gwApiKeys.get(id) == null) {
            event.getInteraction().editButton(event.getButton()
                            .asDisabled()
                            .withLabel("Error")
                            .withStyle(ButtonStyle.DANGER))
                    .queue();
            if (message != null) {
                message.editMessage("Unable to link account.").queue();
            }
            return;
        }
        event.getInteraction().editButton(event.getButton()
                        .asDisabled()
                        .withLabel("Linked")
                        .withStyle(ButtonStyle.SECONDARY))
                .queue();
        if (message != null) {
            message.editMessage("You have successfully *linked* your GW2 account.").queue();
        }
        addToDatabase(gwApiKeys.get(id), member);
        gwApiKeys.remove(id);
    }

    protected static void unlinkProcess(ButtonClickEvent event, Member member) {
        Message message = event.getMessage();
        if (!isLinked(member)) {
            event.getInteraction().editButton(event.getButton()
                            .asDisabled()
                            .withLabel("Error")
                            .withStyle(ButtonStyle.DANGER))
                    .queue();
            if (message != null) {
                message.editMessage("Unable to link account.").queue();
            }
            return;
        }
        event.getInteraction().editButton(event.getButton()
                        .asDisabled()
                        .withLabel("Unlinked")
                        .withStyle(ButtonStyle.SECONDARY))
                .queue();
        if (message != null) {
            message.editMessage("You have successfully *unlinked* your GW2 account.").queue();
        }
        removeFromDatabase(member);
    }

    protected static boolean isGWToken(String gw2Token) {
        if (gw2Token.length() != 72) return false;
        String[] arr = gw2Token.split("-");
        return arr.length == 9;
    }
    /*
    =====================================
    БД МЕТОДЫ
    =====================================
     */
    public static boolean isLinked(Member member) {
        try (Connection connection = Database.getConnection()) {
            final String QUERY = String.format("SELECT * FROM %s WHERE discord_id = %s", Main.TABLE_NAME, member.getUser().getId());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getGWToken(@Nonnull Member member) {
        try (Connection connection = Database.getConnection()) {
            final String QUERY = String.format(
                    "SELECT * FROM %s WHERE discord_id = '%s'",
                    Main.TABLE_NAME, member.getId());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(QUERY);
            rs.next();
            return rs.getString("gw2token");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Не показывай взору свою натуру
    private static void removeFromDatabase(Member member) {
        try (Connection connection = Database.getConnection()) {
            final String QUERY = String.format("DELETE FROM %s WHERE discord_id = ?;", Main.TABLE_NAME);
            try (PreparedStatement statement = connection.prepareStatement(QUERY)) {
                statement.setObject(1, member.getUser().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Не показывай взору свою натуру
    private static void addToDatabase(String gw2Token, Member member) {
        try (Connection connection = Database.getConnection()) {
            final String QUERY = String.format(
                    "INSERT INTO %s (gw2token, discord_id) VALUES (?, ?);",
                    Main.TABLE_NAME);
            try (PreparedStatement statement = connection.prepareStatement(QUERY)) {
                statement.setObject(1, gw2Token);
                statement.setObject(2, member.getUser().getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
