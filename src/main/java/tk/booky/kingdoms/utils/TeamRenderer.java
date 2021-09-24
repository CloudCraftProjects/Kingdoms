package tk.booky.kingdoms.utils;
// Created by booky10 in Kingdoms (17:42 21.09.21)

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.booky.kingdoms.team.KingdomsTeam;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class TeamRenderer implements ChatRenderer {

    private static final Component SEPERATOR = text()
        .append(space())
        .append(text('●', DARK_GRAY))
        .append(space())
        .build();

    private final UserManager users = LuckPermsProvider.get().getUserManager();
    private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        TextComponent.Builder nameBuilder = text().color(GRAY);

        User user = users.getUser(source.getUniqueId());
        if (user != null) {
            String prefixString = user.getCachedData().getMetaData().getPrefix();
            if (prefixString != null) {
                nameBuilder
                    .append(serializer.deserialize(prefixString))
                    .append(SEPERATOR);
            }
        }

        nameBuilder.append(sourceDisplayName.color(GRAY));
        KingdomsTeam team = KingdomsTeam.byMember(source.getUniqueId());
        if (team != null) {
            nameBuilder.append(team.suffixComponent());
        }

        return translatable("chat.type.text", nameBuilder.build(), message);
    }
}

