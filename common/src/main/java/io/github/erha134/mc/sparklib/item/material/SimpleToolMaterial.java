package io.github.erha134.mc.sparklib.item.material;

import lombok.Getter;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public class SimpleToolMaterial implements ToolMaterial {
    @Getter(onMethod_ = {@Override})
    private final int miningLevel;
    @Getter(onMethod_ = {@Override})
    private final int durability;
    @Getter(onMethod_ = {@Override})
    private final float miningSpeedMultiplier;
    @Getter(onMethod_ = {@Override})
    private final float attackDamage;
    @Getter(onMethod_ = {@Override})
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    public SimpleToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.durability = itemDurability;
        this.miningSpeedMultiplier = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
