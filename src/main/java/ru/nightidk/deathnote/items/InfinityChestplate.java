package ru.nightidk.deathnote.items;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.items.base.InfinityArmor;

import static ru.nightidk.deathnote.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityChestplate extends InfinityArmor {
    public InfinityChestplate() {
        super(INFINITY_MATERIAL, Type.CHESTPLATE, new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    private static final Identifier INFINITY_CHESTPLATE_FLIGHT_ABILITY_SOURCE_ID = new Identifier(DeathNote.MOD_ID, "infinity_chestplate");
    private static final AbilitySource INFINITY_CHESTPLATE_ABILITY_SOURCE = Pal.getAbilitySource(INFINITY_CHESTPLATE_FLIGHT_ABILITY_SOURCE_ID, AbilitySource.CONSUMABLE);

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
    }

    public static boolean isAllowingFlight(PlayerEntity player) {
        return INFINITY_CHESTPLATE_ABILITY_SOURCE.grants(player, VanillaAbilities.ALLOW_FLYING) && INFINITY_CHESTPLATE_ABILITY_SOURCE.isActivelyGranting(player, VanillaAbilities.ALLOW_FLYING);
    }

    public static boolean hasIndirectFlight(PlayerEntity player) {
        return VanillaAbilities.CREATIVE_MODE.isEnabledFor(player) || player.isCreative() || player.isSpectator();
    }

    public static void allowFlying(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_CHESTPLATE_ABILITY_SOURCE.grantTo(playerEntity, VanillaAbilities.ALLOW_FLYING);
            playerEntity.setOnGround(true);
        }
    }

    public static void dontAllowFlying(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_CHESTPLATE_ABILITY_SOURCE.revokeFrom(playerEntity, VanillaAbilities.ALLOW_FLYING);
        }
    }
}
