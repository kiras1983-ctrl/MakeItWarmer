package com.kiras.makeitwarmer.registry;

import com.kiras.makeitwarmer.MakeItWarmer;
import com.kiras.makeitwarmer.block.BurzhuykaBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MakeItWarmer.MODID);

    public static final RegistryObject<Block> CYLINDER_BURZHUYKA = BLOCKS.register("cylinder_burzhuyka",
            () -> new BurzhuykaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));

    public static final RegistryObject<Block> WELDED_BURZHUYKA = BLOCKS.register("welded_burzhuyka",
            () -> new BurzhuykaBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0F, 8.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));
}
