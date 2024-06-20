package ru.nightidk.deathnote.items.abilities;

import io.github.ladysnake.pal.PlayerAbility;
import io.github.ladysnake.pal.SimpleAbilityTracker;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Contract;

import java.util.function.Predicate;

public class InfinityAbilityTracker extends SimpleAbilityTracker {
    private final InfinityAbilityTracker.AbilitySetter setter;
    private final Predicate<InfinityAbilitiesProps> getter;

    public InfinityAbilityTracker(PlayerAbility abilityId, PlayerEntity player, InfinityAbilityTracker.AbilitySetter setter, Predicate<InfinityAbilitiesProps> getter) {
        super(abilityId, player);
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    protected void updateState(boolean enabled) {
        super.updateState(enabled);
        updateBacking(enabled);
    }

    private void updateBacking(boolean enabled) {
        this.setter.set(((InfinityAbilitiesContainer) this.player).deathNote$getInfinityAbilities(), enabled);
    }

    @Override
    protected void sync() {
        this.player.sendAbilitiesUpdate();
    }

    @Override
    public boolean isEnabled() {
        return this.getter.test(((InfinityAbilitiesContainer) this.player).deathNote$getInfinityAbilities());
    }

    @FunctionalInterface
    public interface AbilitySetter {
        @Contract(mutates = "param1")
        void set(InfinityAbilitiesProps abilities, boolean enabled);
    }
}
