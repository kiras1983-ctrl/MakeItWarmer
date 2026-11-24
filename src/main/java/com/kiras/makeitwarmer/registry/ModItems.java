package com.kiras.makeitwarmer.registry;

import com.kiras.makeitwarmer.MakeItWarmer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MakeItWarmer.MODID);

    public static final RegistryObject<Item> SHEET_IRON = ITEMS.register("sheet_iron",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STOVEPIPE = ITEMS.register("stovepipe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CYLINDER_BURZHUYKA = ITEMS.register("cylinder_burzhuyka",
            () -> new BlockItem(ModBlocks.CYLINDER_BURZHUYKA.get(), new Item.Properties()));

    public static final RegistryObject<Item> WELDED_BURZHUYKA = ITEMS.register("welded_burzhuyka",
            () -> new BlockItem(ModBlocks.WELDED_BURZHUYKA.get(), new Item.Properties()));
}
