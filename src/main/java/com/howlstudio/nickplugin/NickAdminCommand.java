package com.howlstudio.nickplugin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

/** /nickreset <player>  — staff removes a player's nickname */
public class NickAdminCommand extends AbstractPlayerCommand {
    private final NickManager manager;

    public NickAdminCommand(NickManager manager) {
        super("nickreset", "[Staff] Reset a player's nickname. Usage: /nickreset <player>");
        this.manager = manager;
    }

    @Override
    protected void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref,
                           PlayerRef playerRef, World world) {
        String input = ctx.getInputString().trim();
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            playerRef.sendMessage(Message.raw("§cUsage: /nickreset <player>"));
            return;
        }

        String targetName = parts[1];
        PlayerRef targetRef = Universe.get().getPlayerByUsername(targetName, NameMatching.EXACT);
        UUID targetUuid = targetRef != null ? targetRef.getUuid() : UUID.nameUUIDFromBytes(targetName.getBytes());

        if (manager.hasNick(targetUuid)) {
            manager.clearNick(targetUuid);
            playerRef.sendMessage(Message.raw("§a[Nick] §fRemoved nickname from §e" + targetName + "§f."));
            if (targetRef != null) {
                targetRef.sendMessage(Message.raw("§c[Nick] §fA staff member removed your nickname."));
            }
        } else {
            playerRef.sendMessage(Message.raw("§7[Nick] §f" + targetName + " §7doesn't have a nickname set."));
        }
    }
}
