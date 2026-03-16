package com.howlstudio.nickplugin;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.UUID;

public class NickChatListener {

    private final NickManager manager;

    public NickChatListener(NickManager manager) {
        this.manager = manager;
    }

    public void register() {
        var bus = HytaleServer.get().getEventBus();
        bus.registerGlobal(PlayerReadyEvent.class, this::onJoin);
        bus.registerGlobal(PlayerChatEvent.class, this::onChat);
        bus.registerGlobal(PlayerDisconnectEvent.class, this::onLeave);
    }

    private void onJoin(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        PlayerRef ref = player.getPlayerRef();
        if (ref == null) return;
        UUID uuid = ref.getUuid();
        if (uuid == null) return;
        String username = ref.getUsername() != null ? ref.getUsername() : "Unknown";
        manager.registerPlayer(uuid, username);

        // Remind player of their nick on join
        if (manager.hasNick(uuid)) {
            ref.sendMessage(Message.raw("§7[Nick] §fYour nickname is §r" + manager.getNick(uuid)
                + "§7. Use §e/nick off §7to reset."));
        }
    }

    private void onChat(PlayerChatEvent event) {
        PlayerRef sender = event.getSender();
        if (sender == null) return;
        UUID uuid = sender.getUuid();
        if (uuid == null || !manager.hasNick(uuid)) return;

        // Replace sender name in chat with nickname
        String nick = manager.getNick(uuid);
        String content = event.getContent();

        // Use a custom formatter to show nickname instead of username
        event.setFormatter((recipientRef, msg) ->
            Message.raw("§r" + nick + "§r §7» §f" + msg));
    }

    private void onLeave(PlayerDisconnectEvent event) {
        // No cleanup needed — nicks persist
    }
}
