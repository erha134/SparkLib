package io.github.erha134.mc.sparklib.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryWrapper;
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
    public ItemStack craft(SimpleInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
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
        private final MapCodec<R> codec;
        private final PacketCodec<RegistryByteBuf, R> packetCodec;

        public Serializer(Factory<R> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                            ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output))
                    .apply(instance, this.factory::create));
            this.packetCodec = PacketCodec.ofStatic(this::write, this::read);
        }

//        @Override
//        public R read(Identifier id, JsonObject json) {
//            Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
//            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
//            return this.factory.create(id, input, output);
//        }

        @Override
        public MapCodec<R> codec() {
            return this.codec;
        }

        @Override
        public PacketCodec<RegistryByteBuf, R> packetCodec() {
            return this.packetCodec;
        }

        private R read(RegistryByteBuf buf) {
            Ingredient input = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(buf);
            return this.factory.create(input, output);
        }

        private void write(RegistryByteBuf buf, R recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.input);
            ItemStack.PACKET_CODEC.encode(buf, recipe.output);
        }

        public interface Factory<R extends SingleIngredientRecipe<R>> {
            R create(Ingredient input, ItemStack output);
        }
    }
}
