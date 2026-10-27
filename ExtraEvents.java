package com.example.permscmds;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

/** Lista de jugadores (tab) con prefijo y ping, y posiciones para /back. */
public final class ExtraEvents {
    private int ticks = 0;

    /** Formato de la lista de jugadores: [Prefijo] Nombre [23ms]. Funciona con clientes vanilla. */
    @SubscribeEvent
    public void onTabName(PlayerEvent.TabListNameFormat event) {
        Player p = event.getEntity();
        String nick = PlayerData.nickOf(p.getUUID());
        Component shown = nick != null ? Text.color(nick) : p.getName();
        MutableComponent name = Text.color(PermissionManager.prefixFor(p.getUUID())).append(shown);
        if (Settings.get().tabShowPing && p instanceof ServerPlayer sp) {
            name.append(Text.color(" &7[" + Text.pingColor(sp.latency) + sp.latency + "ms&7]"));
        }
        event.setDisplayName(name);
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) sp.refreshTabListName();
    }

    /** El ping cambia con el tiempo: se refresca la lista cada pocos segundos. */
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (++ticks < Math.max(20, Settings.get().tabRefreshTicks)) return;
        ticks = 0;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            p.refreshTabListName();
        }
    }

    /** /superpickaxe: rompe bloques al instante con un pico en la mano. */
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player p = event.getEntity();
        if (AdminCommands.SUPERPICK.contains(p.getUUID()) && p.getMainHandItem().getItem() instanceof PickaxeItem) {
            event.setNewSpeed(1_000_000F);
        }
    }

    // ---- /back: guardar donde estabas

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) UtilityCommands.remember(sp);
    }

    @SubscribeEvent
    public void onTpCommand(EntityTeleportEvent.TeleportCommand event) {
        if (event.getEntity() instanceof ServerPlayer sp) UtilityCommands.remember(sp);
    }
}
