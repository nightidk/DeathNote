package ru.nightidk.deathnote.register;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import ru.nightidk.deathnote.api.crafting.ISpecialRecipe;

public class SerializerRegister {
    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, ISpecialRecipe.Serializer.ID, ISpecialRecipe.Serializer.INSTANCE);
    }
}
