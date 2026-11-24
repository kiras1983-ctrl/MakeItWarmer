package com.makeitwarmer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

public class BurzhuykaBlockEntity extends BlockEntity implements MenuProvider {
    private final SimpleContainer inventory = new SimpleContainer(3);
    private int litTime = 0;
    private int litDuration = 0;
    private int heatLevel = 0;
    private int closedTicks = 0;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> heatLevel;
                case 3 -> 100;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> litTime = value;
                case 1 -> litDuration = value;
                case 2 -> heatLevel = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public BurzhuykaBlockEntity(BlockPos pos, BlockState state) {
        super(MakeItWarmerMod.BURZHUYKA_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BurzhuykaBlockEntity be) {
        int damper = state.getValue(BurzhuykaBlock.DAMPER);
        boolean wasLit = state.getValue(BurzhuykaBlock.LIT);
        boolean dirty = false;

        if (damper == 0 && be.litTime > 0) {
            be.closedTicks++;
            if (be.closedTicks >= 100) {
                be.litTime = 0;
                be.closedTicks = 0;
                dirty = true;
            }
        } else {
            be.closedTicks = 0;
        }

        if (be.litTime > 0) {
            int burnStep = (damper == 2 && level.getGameTime() % 4 == 0) ? 2 : 1;
            be.litTime = Math.max(0, be.litTime - burnStep);
            if (be.heatLevel < 100) {
                be.heatLevel = Math.min(100, be.heatLevel + (damper == 2 ? 2 : 1));
            }
            dirty = true;
        } else {
            ItemStack fuel = be.inventory.getItem(1);
            if (damper > 0 && !fuel.isEmpty()) {
                int burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
                if (burnTime > 0) {
                    be.litTime = burnTime;
                    be.litDuration = burnTime;
                    fuel.shrink(1);
                    dirty = true;
                }
            } else if (be.heatLevel > 0 && level.getGameTime() % 2 == 0) {
                be.heatLevel--;
                dirty = true;
            }
        }

        boolean isLitNow = be.litTime > 0;
        if (wasLit != isLitNow) {
            level.setBlock(pos, state.setValue(BurzhuykaBlock.LIT, isLitNow), 3);
            dirty = true;
        }

        if (dirty) {
            setChanged(level, pos, state);
        }
    }

    public int getHeatLevel() {
        return heatLevel;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.makeitwarmer.burzhuyka");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FurnaceMenu(containerId, playerInventory, this.inventory, this.dataAccess);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("LitTime", litTime);
        tag.putInt("LitDuration", litDuration);
        tag.putInt("HeatLevel", heatLevel);
        tag.putInt("ClosedTicks", closedTicks);
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        for (int i = 0; i < 3; i++) items.set(i, inventory.getItem(i));
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        litTime = tag.getInt("LitTime");
        litDuration = tag.getInt("LitDuration");
        heatLevel = tag.getInt("HeatLevel");
        closedTicks = tag.getInt("ClosedTicks");
        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        for (int i = 0; i < 3; i++) inventory.setItem(i, items.get(i));
    }
}
