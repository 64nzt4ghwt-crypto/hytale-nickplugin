# NickPlugin for Hytale

Custom player nicknames with full color code support. Players personalize their chat name; staff can reset nicknames.

## Commands

| Command | Description |
|---------|-------------|
| `/nick <name>` | Set your nickname (use `&` for colors) |
| `/nick off` | Reset to your real username |
| `/nickreset <player>` | Remove a player's nickname (staff) |

## Color Codes

Use `&` before a color code in your nickname:

| Code | Color | Code | Effect |
|------|-------|------|--------|
| `&a` | Green | `&l` | Bold |
| `&b` | Aqua | `&o` | Italic |
| `&c` | Red | `&n` | Underline |
| `&e` | Yellow | `&k` | Magic |
| `&6` | Gold | `&r` | Reset |
| `&5` | Purple | | |

**Example:** `/nick &6GoldKing` → displays as **GoldKing** in gold

## Features
- **Color codes** — full `&` color code support in nicknames
- **Banned words** — blocks staff/admin/mod/op impersonation
- **2-20 character limit** — alphanumeric, `_`, `-` only
- **Chat integration** — nickname shown in chat instead of real name
- **Persistent** — nicknames survive server restarts
- **Login reminder** — players notified of their active nickname on join
