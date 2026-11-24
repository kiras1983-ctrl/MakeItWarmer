package com.kiras.makeitwarmer.registry;

import com.kiras.makeitwarmer.MakeItWarmer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MakeItWarmer.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.makeitwarmer.main"))
                    .icon(() -> new ItemStack(ModItems.WELDED_BURZHUYKA.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.SHEET_IRON.get());
                        output.accept(ModItems.STOVEPIPE.get());
                        output.accept(ModItems.CYLINDER_BURZHUYKA.get());
                        output.accept(ModItems.WELDED_BURZHUYKA.get());
                    })
                    .build());
}
