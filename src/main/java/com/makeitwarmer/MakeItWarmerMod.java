package com.makeitwarmer;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(MakeItWarmerMod.MODID)
public class MakeItWarmerMod {
    public static final String MODID = "makeitwarmer";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> CYLINDER_BURZHUYKA = BLOCKS.register("cylinder_burzhuyka",
            () -> new BurzhuykaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BurzhuykaBlock.LIT) ? 13 : 0)));

    public static final RegistryObject<Block> WELDED_BURZHUYKA = BLOCKS.register("welded_burzhuyka",
            () -> new BurzhuykaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 8.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BurzhuykaBlock.LIT) ? 14 : 0)));

    public static final RegistryObject<Item> SHEET_IRON = ITEMS.register("sheet_iron",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STOVEPIPE = ITEMS.register("stovepipe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CYLINDER_BURZHUYKA_ITEM = ITEMS.register("cylinder_burzhuyka",
            () -> new BlockItem(CYLINDER_BURZHUYKA.get(), new Item.Properties()));

    public static final RegistryObject<Item> WELDED_BURZHUYKA_ITEM = ITEMS.register("welded_burzhuyka",
            () -> new BlockItem(WELDED_BURZHUYKA.get(), new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register("makeitwarmer_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.makeitwarmer"))
                    .icon(() -> new ItemStack(WELDED_BURZHUYKA_ITEM.get()))
                    .displayItems((params, output) -> {
                        output.accept(SHEET_IRON.get());
                        output.accept(STOVEPIPE.get());
                        output.accept(CYLINDER_BURZHUYKA_ITEM.get());
                        output.accept(WELDED_BURZHUYKA_ITEM.get());
                    })
                    .build());

    public MakeItWarmerMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
    }
}
