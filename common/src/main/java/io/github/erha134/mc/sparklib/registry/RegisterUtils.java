package io.github.erha134.mc.sparklib.registry;

import dev.architectury.registry.CreativeTabRegistry;
import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class RegisterUtils {
    public static <R extends Recipe<?>> RecipeType<R> createRecipeType(String id) {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return id;
            }
        };
    }

    public static <BE extends BlockEntity> BlockEntityType<BE> createBlockEntityType(BlockEntityType.BlockEntityFactory<BE> factory,
                                                                                     Block... validateBlocks) {

        return BlockEntityType.Builder.create(factory, validateBlocks).build(null);
    }

    public static ItemGroup createGroup(String modId, String id, ItemStackProvider icon) {
        return CreativeTabRegistry.create(
                new Identifier(modId, id),
                icon::sparklib$asStack);
    }

    public static Item createSimpleItem() {
        return createSimpleItem(Item::new);
    }

    public static <I extends Item> I createSimpleItem(Function<Item.Settings, I> factory) {
        return factory.apply(new Item.Settings());
    }

    public static Block createSimpleBlock(Material material) {
        return createSimpleBlock(Block::new, material);
    }

    public static <B extends Block> B createSimpleBlock(Function<AbstractBlock.Settings, B> factory, Material material) {
        return factory.apply(AbstractBlock.Settings.of(material));
    }
}
