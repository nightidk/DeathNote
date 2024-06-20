package ru.nightidk.deathnote.api.crafting;

import com.google.gson.*;
import lombok.Getter;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ISpecialRecipe implements Recipe<Inventory> {

    private final ItemStack output;
    @Getter
    private final List<String> pattern;
    @Getter
    private final List<Pattern> keys;
    private final Identifier id;

    public ISpecialRecipe(List<String> pattern, List<Pattern> keys, ItemStack output, Identifier id) {
        this.output = output;
        this.keys = keys;
        this.pattern = pattern;
        this.id = id;
    }

    public static class Type implements RecipeType<ISpecialRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "special_recipe";
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (world.isClient()) return false;

        int j = 0;
        for (String s : this.pattern) {
            char[] pChars = s.toCharArray();
            for (Character c : pChars) {
                if (String.valueOf(c).isEmpty() || String.valueOf(c).isBlank()) {
                    if (inventory.getStack(j) != ItemStack.EMPTY) return false;
                } else {
                    Pattern p = findPattern(String.valueOf(c));
                    if (p == null) throw new RuntimeException("No pattern for char: " + c + ", id: " + this.id);
                    if (p.getItemStack().getItem() != inventory.getStack(j).getItem()) return false;
                }
                j++;
            }
        }
        return true;
    }

    @Nullable
    private Pattern findPattern(String code) {
        for (Pattern p: this.keys)
            if (Objects.equals(p.getPatternCode(), code))
                return p;
        return null;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return new Identifier(DeathNote.MOD_ID, Type.ID);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(0);
    }

    private static class ISpecialRecipeJsonFormat {
        JsonArray pattern;
        JsonObject key;
        JsonObject output;
    }

    public static class Serializer implements RecipeSerializer<ISpecialRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(DeathNote.MOD_ID, "special");

        @Override
        public ISpecialRecipe read(Identifier id, JsonObject json) {
            ISpecialRecipeJsonFormat recipeJson = new Gson().fromJson(json, ISpecialRecipeJsonFormat.class);

            List<String> patterns = recipeJson.pattern.asList().stream().map(JsonElement::getAsString).toList();
            List<Pattern> keys = new ArrayList<>();

            recipeJson.key.asMap().forEach((s, e) -> {
                JsonObject el = e.getAsJsonObject();
                Item item = Registries.ITEM.getOrEmpty(new Identifier(el.get("item").getAsString())).orElseThrow(() -> new JsonSyntaxException("No such item " + id));
                ItemStack itemStack = new ItemStack(item, 1);
                JsonObject nbt = el.get("nbt").getAsJsonObject();
                if (nbt.size() != 0) {
                    NbtCompound compound = itemStack.getOrCreateNbt();
                    nbt.asMap().forEach((s_, e_) -> compound.putString(s_, e_.getAsString()));
                    itemStack.setNbt(compound);
                }
                keys.add(new Pattern(new Pair<>(s, itemStack)));
            });

            Optional<Item> outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.output.get("item").getAsString()));
            ItemStack output = ItemStack.EMPTY;
            if (outputItem.isPresent())
                output = new ItemStack(outputItem.get(), recipeJson.output.get("count").getAsInt());

            return new ISpecialRecipe(patterns, keys, output, id);
        }

        @Override
        public ISpecialRecipe read(Identifier id, PacketByteBuf buf) {
            List<String> patterns = new ArrayList<>();
            for (int i = 0; i < buf.readInt(); i++)
                patterns.set(i, buf.readString());

            List<Pattern> keys = new ArrayList<>();
            for (int i = 0; i < buf.readInt(); i++)
                keys.set(i, new Pattern(new Pair<>(buf.readString(), buf.readItemStack())));

            ItemStack output = buf.readItemStack();

            return new ISpecialRecipe(patterns, keys, output, id);
        }

        @Override
        public void write(PacketByteBuf buf, ISpecialRecipe recipe) {
            buf.writeInt(recipe.getPattern().size());

            for (String p : recipe.getPattern())
                buf.writeString(p);

            buf.writeInt(recipe.getKeys().size());

            for (Pattern pat: recipe.getKeys()) {
                buf.writeString(pat.getPatternCode());
                buf.writeItemStack(pat.getItemStack());
            }

            buf.writeItemStack(recipe.getOutput(null));
        }
    }
}
