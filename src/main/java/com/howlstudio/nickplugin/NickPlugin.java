package com.howlstudio.nickplugin;

import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

/**
 * NickPlugin — Custom player nicknames with color code support.
 *
 * Features:
 *   - Players set colorful custom nicknames with &color codes
 *   - Nickname shown in chat instead of real username
 *   - Banned words filter (admin, owner, staff, mod, op)
 *   - 2-20 character limit, alphanumeric + _ -
 *   - Persistent across restarts
 *   - Staff command to reset any player's nickname
 *   - Login reminder of current nickname
 *
 * Commands:
 *   /nick <name|off>    — set or clear your nickname
 *   /nickreset <player> — [Staff] remove a player's nickname
 *
 * Color codes (use & prefix):
 *   &0-9, &a-f = standard colors
 *   &l = bold, &o = italic, &n = underline, &k = magic, &r = reset
 */
public final class NickPlugin extends JavaPlugin {

    private NickManager manager;

    public NickPlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        System.out.println("[Nick] Loading...");

        manager = new NickManager(getDataDirectory());

        CommandManager cmd = CommandManager.get();
        cmd.register(new NickCommand(manager));
        cmd.register(new NickAdminCommand(manager));

        new NickChatListener(manager).register();

        System.out.println("[Nick] Ready!");
    }

    @Override
    protected void shutdown() {
        if (manager != null) {
            manager.save();
            System.out.println("[Nick] Saved.");
        }
    }
}
