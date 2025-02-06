package io.github.erha134.mc.sparklib.recipe.shield;

import io.github.erha134.mc.sparklib.item.shield.SShieldItem;
import io.github.erha134.mc.sparklib.recipe.SRecipeSerializers;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
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
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class SShieldDecorationRecipe extends ShieldDecorationRecipe {
    public SShieldDecorationRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
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

                    BannerPatternsComponent bannerPatternsComponent = stack.getOrDefault(DataComponentTypes.BANNER_PATTERNS,
                            BannerPatternsComponent.DEFAULT);
                    if (!bannerPatternsComponent.layers().isEmpty()) {
                        return false;
                    }

                    shield = stack;
                }
            }
        }

        return !shield.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
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
            shield.set(DataComponentTypes.BANNER_PATTERNS, banner.get(DataComponentTypes.BANNER_PATTERNS));
            shield.set(DataComponentTypes.BASE_COLOR, ((BannerItem) banner.getItem()).getColor());
        }

        return shield;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SRecipeSerializers.SPARK_LIB_SHIELD_DECORATION.get();
    }
}
