package ru.nightidk.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.nightidk.items.base.InfinityArmor;

import java.util.concurrent.atomic.AtomicInteger;

public class ArmorUtils {
    public static boolean hasFullInfinityArmor(ServerPlayerEntity player) {
        AtomicInteger countReduce = new AtomicInteger();
        player.getInventory().armor.forEach(i -> {
            if (i.getItem() instanceof InfinityArmor) countReduce.getAndIncrement();
        });
        return countReduce.get() == 4;
    }
}
