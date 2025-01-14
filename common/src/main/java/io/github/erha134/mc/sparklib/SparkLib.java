package io.github.erha134.mc.sparklib;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.item.shield.DamageableShield;
import io.github.erha134.mc.sparklib.recipe.SRecipeSerializers;
import io.github.erha134.mc.sparklib.recipe.SRecipeTypes;
import io.github.erha134.mc.sparklib.util.VersionChecker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public final class SparkLib {
    public static final String MOD_ID = "sparklib";
    public static final String MOD_NAME = "Spark Lib";
    public static final String MOD_VERSION = Platform.getMod(MOD_ID).getVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        SRecipeTypes.register();
        SRecipeSerializers.register();

        EntityEvent.LIVING_HURT.register((target, damageSource, amount) -> {
            if (target instanceof PlayerEntity player) {
                ItemStack stack = player.getActiveItem();
                Entity attackSource = damageSource.getSource();

                if (!stack.isEmpty() && stack.getItem() instanceof DamageableShield shield && attackSource instanceof LivingEntity livingEntity) {
                    shield.damageAttacker(stack, player, livingEntity, amount);
                }
            }

            return EventResult.pass();
        });

        if (Platform.isDevelopmentEnvironment()) {
            CompletableFuture.supplyAsync(() -> VersionChecker.doCheck("47.3.22",
                            "https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json"))
                    .thenAccept(c -> LOGGER.info("[Version Checker Test] status: {}, current: {}, recommended: {}, latest: {}",
                            c.getStatus(),
                            c.getCurrent(),
                            c.getRecommended(),
                            c.getLatest()));
        }
    }

    private SparkLib() {
        // NO-OP
    }
}
