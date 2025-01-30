package io.github.erha134.mc.sparklib.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SingleIngredientRecipe<R extends SingleIngredientRecipe<R>> implements Recipe<SimpleInventory> {
    private final RecipeSerializer<R> serializer;
    private final RecipeType<R> type;
    protected final Ingredient input;
    protected final ItemStack output;

    public SingleIngredientRecipe(RecipeSerializer<R> serializer,
                                  RecipeType<R> type,
                                  Ingredient input,
                                  ItemStack output) {
        this.serializer = serializer;
        this.type = type;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }

        return this.input.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    public static class Serializer<R extends SingleIngredientRecipe<R>> implements RecipeSerializer<R> {
        private final Factory<R> factory;
        private final Codec<R> codec;

        public Serializer(Factory<R> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.create(instance -> instance.group(
                            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                            ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output))
                    .apply(instance, this.factory::create));
        }

//        @Override
//        public R read(Identifier id, JsonObject json) {
//            Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
//            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
//            return this.factory.create(id, input, output);
//        }

        @Override
        public Codec<R> codec() {
            return this.codec;
        }

        @Override
        public R read(PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            return this.factory.create(input, output);
        }

        @Override
        public void write(PacketByteBuf buf, R recipe) {
            recipe.input.write(buf);
            buf.writeItemStack(recipe.output);
        }

        public interface Factory<R extends SingleIngredientRecipe<R>> {
            R create(Ingredient input, ItemStack output);
        }
    }
}
