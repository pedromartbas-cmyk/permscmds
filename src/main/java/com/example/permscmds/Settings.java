package com.example.permscmds;

/** config/permscmds/settings.json */
public final class Settings {
    /** Los operadores nivel 4 se saltan todas las comprobaciones (evita quedarte fuera). */
    public boolean opsBypass = true;
    public int teleportCooldownSeconds = 5;
    public int tpaTimeoutSeconds = 60;
    public int defaultMaxHomes = 3;
    /** Muestra el ping (ms) junto al nombre en la lista de jugadores (tab). */
    public boolean tabShowPing = true;
    /** Cada cuantos ticks se refresca la lista de jugadores (20 ticks = 1 s). */
    public int tabRefreshTicks = 60;
    public String noPermissionMessage = "&cNo tienes permiso para hacer eso.";

    private static Settings instance = new Settings();

    public static Settings get() {
        return instance;
    }

    public static void load() {
        instance = JsonStore.load("settings.json", Settings.class, Settings::new);
    }
}
