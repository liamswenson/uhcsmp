package com.liamswenson.uhcsmp.mixin;

import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isHardcore()Z"))
    private void tempBanPlayer(CallbackInfo ci) {
        Date banExpiry = new Date(new Date().getTime() + 16 * 60 * 60 * 1000);

        ServerPlayerEntity player = this.player;

        BannedPlayerEntry bannedPlayerEntry = new BannedPlayerEntry(
                player.getGameProfile(),
                new Date(),
                null,
                banExpiry,
                "You died!"
        );
        player.server.getPlayerManager().getUserBanList().add(bannedPlayerEntry);

        Text disconnectReason = Text.of(
                "You are banned from this server.\n" +
                "Reason: You died!\n" +
                "Your ban will be removed on " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(banExpiry)
        );
        player.networkHandler.disconnect(disconnectReason);
    }

    @Redirect(method = "onClientStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isHardcore()Z"))
    private boolean setIsHardcoreFalse(MinecraftServer server) {
        return false;
    }
}