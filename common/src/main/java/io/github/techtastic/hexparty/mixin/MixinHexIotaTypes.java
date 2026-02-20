package io.github.techtastic.hexparty.mixin;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import io.github.techtastic.hexparty.casting.hexpartyIotaTypes;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(HexIotaTypes.class)
public class MixinHexIotaTypes {
    @Inject(method = "registerTypes", at = @At("RETURN"), remap = false)
    private static void hexsky$injectRegistration(BiConsumer<ActionRegistryEntry, ResourceLocation> r, CallbackInfo ci) {
        hexpartyIotaTypes.INSTANCE.register();
    }
}
