package com.howlstudio.nickplugin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/** /nick <nickname|off>  — set or clear your nickname */
public class NickCommand extends AbstractPlayerCommand {
    private final NickManager manager;

    public NickCommand(NickManager manager) {
        super("nick", "Set your display nickname. Usage: /nick <name|off>  (Use & for colors: &aGreenName)");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        UUID uuid = playerRef.getUuid();
        if (uuid == null) return;

        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+", 2);

        if (parts.length < 2) {
            playerRef.sendMessage(Message.raw("§cUsage: /nick <name|off>"));
            playerRef.sendMessage(Message.raw("§7Color codes: §e&a§7=green §e&b§7=aqua §e&c§7=red §e&e§7=yellow §e&6§7=gold §e&5§7=purple"));
            if (manager.hasNick(uuid)) {
                playerRef.sendMessage(Message.raw("§7Your current nick: §r" + manager.getNick(uuid)));
            }
            return;
        }

        String rawNick = parts[1];

        if (rawNick.equalsIgnoreCase("off") || rawNick.equalsIgnoreCase("reset") || rawNick.equalsIgnoreCase("clear")) {
            if (!manager.hasNick(uuid)) {
                playerRef.sendMessage(Message.raw("§7You don't have a nickname set."));
            } else {
                manager.clearNick(uuid);
                playerRef.sendMessage(Message.raw("§a[Nick] §fYour nickname has been removed."));
            }
            return;
        }

        // Allow color codes for all players (server-configurable — can restrict via permissions)
        String err = manager.setNick(uuid, rawNick, true);
        if (err != null) {
            playerRef.sendMessage(Message.raw("§c[Nick] " + err));
            return;
        }

        String display = manager.getNick(uuid);
        playerRef.sendMessage(Message.raw("§a[Nick] §fYour nickname is now: §r" + display));
        playerRef.sendMessage(Message.raw("§7Use §e/nick off §7to reset to your real name."));
    }
}
