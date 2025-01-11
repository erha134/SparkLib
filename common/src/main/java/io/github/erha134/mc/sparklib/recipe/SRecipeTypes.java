package io.github.erha134.mc.sparklib.recipe;

import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.recipe.shield.SShieldDecorationRecipe;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import io.github.erha134.mc.sparklib.registry.impl.RecipeTypeDeferredRegister;
import net.minecraft.recipe.RecipeType;

public final class SRecipeTypes {
    public static final RecipeTypeDeferredRegister RECIPE_TYPE = new RecipeTypeDeferredRegister(SparkLib.MOD_ID);

    public static final WrappedRegistrySupplier<RecipeType<SShieldDecorationRecipe>> SPARK_LIB_SHIELD_DECORATION =
            RECIPE_TYPE.registerType("spark_lib_shield_decoration");

    public static void register() {
        RECIPE_TYPE.register();
    }
}
