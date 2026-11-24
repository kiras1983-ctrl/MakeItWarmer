package com.makeitwarmer.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BurzhuykaBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE_CYLINDER = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape SHAPE_WELDED = Block.box(1.5D, 0.0D, 1.0D, 14.5D, 15.0D, 15.0D);

    private final int tier;

    public BurzhuykaBlock(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.FALSE));
    }

    public int getTier() {
        return tier;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return tier == 1 ? SHAPE_CYLINDER : SHAPE_WELDED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Temporary Step 1 toggle with Flint and Steel or empty hand shift-click before Step 2 GUI
        ItemStack held = player.getItemInHand(hand);
        if (!state.getValue(LIT) && held.is(Items.FLINT_AND_STEEL)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LIT, true), 3);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (state.getValue(LIT) && player.isShiftKeyDown() && held.isEmpty()) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LIT, false), 3);
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.7F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 1.05D;
        double z = pos.getZ() + 0.5D;

        if (random.nextDouble() < 0.15D) {
            level.playLocalSound(x, pos.getY() + 0.4D, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 0.8F, 1.0F, false);
        }

        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                x + (random.nextDouble() - 0.5D) * 0.12D,
                y,
                z + (random.nextDouble() - 0.5D) * 0.12D,
                0.0D, 0.04D, 0.0D);
    }
}
