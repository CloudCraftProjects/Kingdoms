package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (17:42 21.09.21)

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.booky.kingdoms.team.KingdomsTeam;

import static net.kyori.adventure.text.Component.translatable;

public class TeamRenderer implements ChatRenderer {

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        KingdomsTeam team = KingdomsTeam.byMember(source.getUniqueId());
        return translatable("chat.type.text", team != null ? sourceDisplayName.append(team.suffixComponent()) : sourceDisplayName, message);
    }
}

