package io.github.erha134.mc.sparklib.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.recipe.shield.SShieldDecorationRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public final class SRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE;

    public static final RegistrySupplier<RecipeType<SShieldDecorationRecipe>> SPARK_LIB_SHIELD_DECORATION;

    static {
        RECIPE_TYPE = DeferredRegister.create(SparkLib.MOD_ID, Registry.RECIPE_TYPE_KEY);
        SPARK_LIB_SHIELD_DECORATION = RECIPE_TYPE.register("spark_lib_shield_decoration", () -> new RecipeType<>() {
            @Override
            public String toString() {
                return "spark_lib_shield_decoration";
            }
        });
    }

    public static void register() {
        RECIPE_TYPE.register();
    }
}
