package com.howlstudio.nickplugin;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NickManager {

    private final Path dataDir;
    private final Map<UUID, String> nicks = new ConcurrentHashMap<>();      // uuid → display nick (with color codes)
    private final Map<UUID, String> usernames = new ConcurrentHashMap<>();  // uuid → real username

    // Disallowed words in nicknames
    private static final List<String> BANNED_PATTERNS = List.of("admin", "owner", "staff", "mod", "op");

    public NickManager(Path dataDir) {
        this.dataDir = dataDir;
        load();
    }

    public void registerPlayer(UUID uuid, String username) {
        usernames.put(uuid, username);
    }

    /** Returns error string or null on success. Strip § color codes unless player has permission. */
    public String setNick(UUID uuid, String raw, boolean allowColors) {
        if (raw == null || raw.isBlank()) return "Nickname cannot be empty.";

        // Strip or validate color codes
        String display = allowColors ? translateColors(raw) : stripColors(raw);
        String plain = stripColors(display).toLowerCase();

        if (plain.length() < 2 || plain.length() > 20) return "Nickname must be 2-20 characters.";
        if (!plain.matches("[a-z0-9_\\-]+")) return "Nickname can only contain letters, numbers, _ and -.";

        for (String banned : BANNED_PATTERNS) {
            if (plain.contains(banned)) return "Nickname contains a restricted word: §f" + banned;
        }

        nicks.put(uuid, display);
        save();
        return null;
    }

    public void clearNick(UUID uuid) {
        nicks.remove(uuid);
        save();
    }

    public String getNick(UUID uuid) {
        return nicks.get(uuid);
    }

    public String getDisplayName(UUID uuid) {
        String nick = nicks.get(uuid);
        if (nick != null) return nick;
        return usernames.getOrDefault(uuid, "Unknown");
    }

    public boolean hasNick(UUID uuid) {
        return nicks.containsKey(uuid);
    }

    /** Translate & color codes to § */
    public static String translateColors(String input) {
        return input.replace("&", "§");
    }

    public static String stripColors(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    @SuppressWarnings("unchecked")
    private void load() {
        Path file = dataDir.resolve("nicks.dat");
        if (!Files.exists(file)) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            nicks.putAll((Map<UUID, String>) ois.readObject());
        } catch (Exception ignored) {}
    }

    public void save() {
        try {
            Files.createDirectories(dataDir);
            try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(dataDir.resolve("nicks.dat")))) {
                oos.writeObject(new HashMap<>(nicks));
            }
        } catch (IOException ignored) {}
    }
}
