package ru.nightidk.deathnote.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nightidk.deathnote.items.InfinityBoots;
import ru.nightidk.deathnote.items.abilities.InfinityAbilitiesProps;
import ru.nightidk.deathnote.items.abilities.InfinityAbilitiesContainer;
import ru.nightidk.deathnote.listeners.both.ModClientOrServerEventListener;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements InfinityAbilitiesContainer {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract void incrementStat(Identifier stat);

    @Shadow public abstract void addExhaustion(float exhaustion);

    @Shadow @Final private PlayerAbilities abilities;

    @Unique
    private final InfinityAbilitiesProps infAbilities = new InfinityAbilitiesProps();

    @Unique
    private final PlayerEntity player = (PlayerEntity) (Object) this;

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

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        ModClientOrServerEventListener.clientArmorCheck(player);
    }

    @Unique
    @Override
    public InfinityAbilitiesProps deathNote$getInfinityAbilities() {
        return this.infAbilities;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteEntityToNBT(NbtCompound nbt, CallbackInfo ci) {
        this.infAbilities.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadEntityFromNBT(NbtCompound nbt, CallbackInfo ci) {
        this.infAbilities.readNbt(nbt);
    }
}
