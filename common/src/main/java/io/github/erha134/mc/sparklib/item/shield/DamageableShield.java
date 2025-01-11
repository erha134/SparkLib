package io.github.erha134.mc.sparklib.item.shield;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface DamageableShield {
    void damageAttacker(ItemStack stack, PlayerEntity player, LivingEntity attacker, float damage);
}
