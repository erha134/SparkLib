package io.github.erha134.mc.sparklib.item.shield;

import lombok.Getter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class SShieldItem extends ShieldItem {
    protected final ToolMaterial toolMaterial;

    public SShieldItem(Item.Settings settings, ToolMaterial toolMaterial) {
        super(settings);
        this.toolMaterial = toolMaterial;
    }

    @Override
    public int getEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.toolMaterial.getRepairIngredient().test(ingredient);
    }

    public boolean supportsBanner() {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.supportsBanner()) {
            super.appendTooltip(stack, world, tooltip, context);
        }
    }
}
