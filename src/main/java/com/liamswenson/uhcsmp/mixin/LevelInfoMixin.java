package com.liamswenson.uhcsmp.mixin;

import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelInfo.class)
public abstract class LevelInfoMixin {
    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private static boolean setHardcore(boolean hardcore) {
        return true;
    }
}