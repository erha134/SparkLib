package io.github.erha134.mc.sparklib;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.item.shield.DamageableShield;
import io.github.erha134.mc.sparklib.recipe.SRecipeSerializers;
import io.github.erha134.mc.sparklib.recipe.SRecipeTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    private SparkLib() {
        // NO-OP
    }
}
