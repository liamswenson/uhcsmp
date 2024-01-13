package com.liamswenson.uhcsmp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class UHCSMP implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
			if (!(livingEntity instanceof ServerPlayerEntity player)) { return; }

			MinecraftServer server = player.getServer();
			if (server == null) { return; }

			BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(player.getGameProfile());
			server.getPlayerManager().getUserBanList().add(bannedPlayerEntry);
			player.networkHandler.disconnect(Text.of("You Died!"));
		});
	}
}