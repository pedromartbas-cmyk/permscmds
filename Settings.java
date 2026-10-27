package com.example.permscmds;

/** config/permscmds/settings.json */
public final class Settings {
    /** Los operadores nivel 4 se saltan todas las comprobaciones (evita quedarte fuera). */
    public boolean opsBypass = true;
    public int tpaTimeoutSeconds = 60;
    public int defaultMaxHomes = 3;
    /** Muestra el ping (ms) junto al nombre en la lista de jugadores (tab). */
    public boolean tabShowPing = true;
    /** Cada cuantos ticks se refresca la lista de jugadores (20 ticks = 1 s). */
    public int tabRefreshTicks = 60;
    /** /rtp: radio maximo desde el centro del borde del mundo. 0 = sin limite (hasta el borde del mundo). */
    public int rtpMaxRadius = 0;
    /** /rtp: radio minimo desde el centro del borde del mundo. */
    public int rtpMinRadius = 0;
    /** /rtp: intentos de buscar un sitio seguro antes de rendirse. */
    public int rtpAttempts = 25;
    public int nickMaxLength = 16;
    public int spawnMobMax = 50;
    public int explodeMaxPower = 20;
    /** Distancia maxima a la que se "mira" un bloque (/jump, /spawnmob). */
    public int lookRange = 256;
    public String noPermissionMessage = "&cNo tienes permiso para hacer eso.";

    private static Settings instance = new Settings();

    public static Settings get() {
        return instance;
    }

    public static void load() {
        instance = JsonStore.load("settings.json", Settings.class, Settings::new);
    }
}
