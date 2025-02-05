package io.github.erha134.mc.sparklib.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.recipe.shield.SShieldDecorationRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

public final class SRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;

    public static final RegistrySupplier<RecipeSerializer<SShieldDecorationRecipe>> SPARK_LIB_SHIELD_DECORATION;

    static {
        RECIPE_SERIALIZERS = DeferredRegister.create(SparkLib.MOD_ID, Registry.RECIPE_SERIALIZER_KEY);
        SPARK_LIB_SHIELD_DECORATION = RECIPE_SERIALIZERS.register("spark_lib_shield_decoration", () ->
                new SpecialRecipeSerializer<>(SShieldDecorationRecipe::new));
    }

    public static void register() {
        RECIPE_SERIALIZERS.register();
    }
}
