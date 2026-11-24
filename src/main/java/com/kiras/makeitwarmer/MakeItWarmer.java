package com.kiras.makeitwarmer;

import com.kiras.makeitwarmer.registry.ModBlocks;
import com.kiras.makeitwarmer.registry.ModCreativeTabs;
import com.kiras.makeitwarmer.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MakeItWarmer.MODID)
public class MakeItWarmer {
    public static final String MODID = "makeitwarmer";

    public MakeItWarmer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
