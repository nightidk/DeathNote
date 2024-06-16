package ru.nightidk.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nightidk.items.InfinityBoots;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract void incrementStat(Identifier stat);

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void jump(CallbackInfo ci) {
        Item boots = getEquippedStack(EquipmentSlot.FEET).getItem();
        if (boots instanceof InfinityBoots) {
            ci.cancel();
            Vec3d vec3d = this.getVelocity();
            float yVelocity = 0.42F * getJumpVelocityMultiplier() + InfinityBoots.JUMP_MODIFIER;
            this.setVelocity(vec3d.x, yVelocity, vec3d.z);
            if (this.isSprinting()) {
                float f = this.getYaw() * 0.017453292F;
                this.setVelocity(this.getVelocity().add(-MathHelper.sin(f) * 0.2F, 0.0, MathHelper.cos(f) * 0.2F));
            }

            this.velocityDirty = true;

            incrementStat(Stats.JUMP);
            if (this.isSprinting()) {
                addExhaustion(0.2F);
            } else {
                addExhaustion(0.05F);
            }
        }
    }
}
