package ru.nightidk.deathnote.register;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.api.crafting.ISpecialRecipe;

public class RecipeRegister {
    public static RecipeType<ISpecialRecipe> INFINITY_TABLE_RECIPE;

    public static void register() {
        INFINITY_TABLE_RECIPE = Registry.register(Registries.RECIPE_TYPE, new Identifier(DeathNote.MOD_ID, ISpecialRecipe.Type.ID), ISpecialRecipe.Type.INSTANCE);
    }
}
