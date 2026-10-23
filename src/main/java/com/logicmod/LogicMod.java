package com.logicmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class LogicMod implements ModInitializer {
    public static final String MOD_ID = "logic";
    public static final Item LOGIC_KATANA = registerKatana();

    private static Item registerKatana() {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, "logic_katana");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);

        Item item = new Item(new Item.Properties().setId(key)) {
            @Override
            public InteractionResult use(Level level, Player player, InteractionHand hand) {
                if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                    double x = player.getX();
                    double y = player.getY() + 1.0;
                    double z = player.getZ();

                    serverLevel.sendParticles(ParticleTypes.DUST,
                            x, y, z, 24, 0.45, 0.5, 0.45, 1.0);
                    serverLevel.sendParticles(ParticleTypes.END_ROD,
                            x, y, z, 10, 0.35, 0.45, 0.35, 0.02);
                }
                return InteractionResult.SUCCESS;
            }
        };
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(LOGIC_KATANA));
    }
}
