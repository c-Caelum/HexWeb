package io.github.techtastic.hexparty.mixin;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import io.github.techtastic.hexparty.casting.hexpartyActions;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(HexActions.class)
public class MixinHexActions {
    @Inject(method = "register", at = @At("RETURN"), remap = false)
    private static void hexsky$injectRegistration(BiConsumer<ActionRegistryEntry, ResourceLocation> r, CallbackInfo ci) {
        hexpartyActions.INSTANCE.register();
    }
}
