package ru.nightidk.items;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import ru.nightidk.DeathNote;
import ru.nightidk.items.abilities.InfinityAbilities;
import ru.nightidk.items.base.InfinityArmor;

import static ru.nightidk.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityHelmet extends InfinityArmor {

    private static final Identifier INFINITY_HELMET_ABILITY_SOURCE_ID = new Identifier(DeathNote.MOD_ID, "infinity_helmet");
    private static final AbilitySource INFINITY_HELMET_ABILITY_SOURCE = Pal.getAbilitySource(INFINITY_HELMET_ABILITY_SOURCE_ID);

    public InfinityHelmet() {
        super(INFINITY_MATERIAL, Type.HELMET, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    public static boolean isAllowingNightVision(PlayerEntity player) {
        return INFINITY_HELMET_ABILITY_SOURCE.grants(player, InfinityAbilities.NIGHT_VISION) && INFINITY_HELMET_ABILITY_SOURCE.isActivelyGranting(player, InfinityAbilities.NIGHT_VISION);
    }

    public static void allowNightVision(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_HELMET_ABILITY_SOURCE.grantTo(playerEntity, InfinityAbilities.NIGHT_VISION);
            if (playerEntity.hasStatusEffect(StatusEffects.NIGHT_VISION))
                playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, false, false, false);
            StatusEffectInstance statusEffect = playerEntity.getStatusEffect(StatusEffects.NIGHT_VISION);
            if (statusEffect == null)
                playerEntity.addStatusEffect(statusEffectInstance, null);
        }
    }

    public static void dontAllowNightVision(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_HELMET_ABILITY_SOURCE.revokeFrom(playerEntity, InfinityAbilities.NIGHT_VISION);
            playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
