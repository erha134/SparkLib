package io.github.erha134.mc.sparklib.mixin.fabric.inject.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.GameVersion;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;

import java.nio.file.Path;

@Mixin(value = FabricDataGenerator.class, remap = false)
public abstract class FabricDataGeneratorMixin extends DataGenerator {
    public FabricDataGeneratorMixin(Path outputPath, GameVersion gameVersion, boolean ignoreCache) {
        super(outputPath, gameVersion, ignoreCache);
    }

    @ApiStatus.Internal
    @Override
    public Pack createVanillaPack(boolean shouldRun) {
        return super.createVanillaPack(shouldRun);
    }

    @ApiStatus.Internal
    @Override
    public Pack createVanillaSubPack(boolean shouldRun, String packName) {
        return super.createVanillaSubPack(shouldRun, packName);
    }
}
