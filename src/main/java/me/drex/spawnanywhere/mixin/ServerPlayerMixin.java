package me.drex.spawnanywhere.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import me.drex.spawnanywhere.SpawnAnywhere;
import me.drex.spawnanywhere.data.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Shadow
    @Final
    public MinecraftServer server;

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "adjustSpawnLocation", at = @At("HEAD"), cancellable = true)
    public void replaceSpawnLocation(ServerLevel serverLevel, BlockPos blockPos, CallbackInfoReturnable<BlockPos> cir) {
        Optional<Location> optional = SpawnAnywhere.DATA.spawnLocation();
        Optional<ServerLevel> spawnLocation = optional.map(location -> server.getLevel(location.dimension()));
        if (spawnLocation.isPresent()) {
            cir.setReturnValue(optional.get().pos());
        }
    }
}
