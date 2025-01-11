package io.github.erha134.mc.sparklib.recipe.shield;

import io.github.erha134.mc.sparklib.item.shield.SShieldItem;
import io.github.erha134.mc.sparklib.recipe.SRecipeSerializers;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShieldDecorationRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SShieldDecorationRecipe extends ShieldDecorationRecipe {
    public SShieldDecorationRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    @Override
    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        ItemStack shield = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack stack = recipeInputInventory.getStack(i);

            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof BannerItem) {
                    if (!banner.isEmpty()) {
                        return false;
                    }

                    banner = stack;
                } else {
                    if (!(item instanceof SShieldItem)) {
                        return false;
                    }

                    if (!shield.isEmpty()) {
                        return false;
                    }

                    if (BlockItem.getBlockEntityNbt(stack) != null) {
                        return false;
                    }

                    shield = stack;
                }
            }
        }

        return !shield.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack stack = recipeInputInventory.getStack(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    banner = stack;
                } else if (stack.getItem() instanceof SShieldItem shieldItem && shieldItem.supportsBanner()) {
                    shield = stack.copy();
                }
            }
        }

        if (!shield.isEmpty()) {
            NbtCompound blockEntityNbt = BlockItem.getBlockEntityNbt(banner);
            NbtCompound nbt = (blockEntityNbt == null ? new NbtCompound() : blockEntityNbt.copy());
            nbt.putInt("Base", ((BannerItem) banner.getItem()).getColor().getId());
            BlockItem.setBlockEntityNbt(shield, BlockEntityType.BANNER, nbt);
        }

        return shield;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SRecipeSerializers.SPARK_LIB_SHIELD_DECORATION.get();
    }
}
