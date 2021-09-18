package tk.booky.kingdoms.commands.teleport;
// Created by booky10 in CraftAttack (14:50 01.03.21)

import dev.jorel.commandapi.CommandAPICommand;
import tk.booky.kingdoms.utils.KingdomsManager;

public class TeleportSubCommand extends CommandAPICommand {

    public TeleportSubCommand(KingdomsManager manager) {
        super("teleport");
        withPermission("kingdoms.command.teleport").withAliases("tp");

        withSubcommand(new BedSubCommand(manager));
        withSubcommand(new EndSubCommand(manager));
        withSubcommand(new SpawnSubCommand(manager));
        withSubcommand(new NetherSubCommand(manager));
    }
}
