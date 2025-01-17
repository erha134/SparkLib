package io.github.erha134.mc.sparklib.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class SingleIngredientRecipe<R extends SingleIngredientRecipe<R>> implements Recipe<SimpleInventory> {
    private final RecipeSerializer<R> serializer;
    private final RecipeType<R> type;
    private final Identifier id;
    protected final Ingredient input;
    protected final ItemStack output;

    public SingleIngredientRecipe(RecipeSerializer<R> serializer,
                                  RecipeType<R> type,
                                  Identifier id,
                                  Ingredient input,
                                  ItemStack output) {
        this.serializer = serializer;
        this.type = type;
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output.copy();
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
        return this.output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
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

        public Serializer(Factory<R> factory) {
            this.factory = factory;
        }

        @Override
        public R read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            return this.factory.create(id, input, output);
        }

        @Override
        public R read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            return this.factory.create(id, input, output);
        }

        @Override
        public void write(PacketByteBuf buf, R recipe) {
            recipe.input.write(buf);
            buf.writeItemStack(recipe.output);
        }

        public interface Factory<R extends SingleIngredientRecipe<R>> {
            R create(Identifier id, Ingredient input, ItemStack output);
        }
    }
}
