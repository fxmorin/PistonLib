package ca.fxco.pistonlib.blocks.pistons.basePiston;

import java.util.Arrays;
import java.util.function.BiPredicate;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamilyMember;
import ca.fxco.pistonlib.base.ModTags;
import com.mojang.serialization.MapCodec;
import lombok.Getter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static ca.fxco.pistonlib.PistonLib.DIRECTIONS;

public class BasicPistonArmBlock extends DirectionalBlock implements PistonFamilyMember {

    // This is the BASIC ARM BLOCK that you should be extending to create your own arm blocks

    public static final BooleanProperty SHORT = BlockStateProperties.SHORT;

    // TODO: Grab there directly instead of using a lookup since it should be faster now that there is no union
    protected static final VoxelShape UP_ARM_SHAPE = Block.box(6.0, -4.0, 6.0, 10.0, 12.0, 10.0);
    protected static final VoxelShape DOWN_ARM_SHAPE = Block.box(6.0, 4.0, 6.0, 10.0, 20.0, 10.0);
    protected static final VoxelShape SOUTH_ARM_SHAPE = Block.box(6.0, 6.0, -4.0, 10.0, 10.0, 12.0);
    protected static final VoxelShape NORTH_ARM_SHAPE = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 20.0);
    protected static final VoxelShape EAST_ARM_SHAPE = Block.box(-4.0, 6.0, 6.0, 12.0, 10.0, 10.0);
    protected static final VoxelShape WEST_ARM_SHAPE = Block.box(4.0, 6.0, 6.0, 20.0, 10.0, 10.0);
    protected static final VoxelShape SHORT_UP_ARM_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);
    protected static final VoxelShape SHORT_DOWN_ARM_SHAPE = Block.box(6.0, 4.0, 6.0, 10.0, 16.0, 10.0);
    protected static final VoxelShape SHORT_SOUTH_ARM_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 12.0);
    protected static final VoxelShape SHORT_NORTH_ARM_SHAPE = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 16.0);
    protected static final VoxelShape SHORT_EAST_ARM_SHAPE = Block.box(0.0, 6.0, 6.0, 12.0, 10.0, 10.0);
    protected static final VoxelShape SHORT_WEST_ARM_SHAPE = Block.box(4.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    private static final VoxelShape[] SHORT_ARM_SHAPES = getArmShapes(true);
    private static final VoxelShape[] ARM_SHAPES = getArmShapes(false);

    BiPredicate<BlockState,BlockState> OR_IS_ATTACHED = (state, selfState) ->
            state.is(ModTags.MOVING_PISTONS) && state.getValue(FACING) == selfState.getValue(FACING);

    public static VoxelShape getArmShape(Direction direction, boolean shortArm) {
        return switch (direction) {
            default -> shortArm ? SHORT_DOWN_ARM_SHAPE : DOWN_ARM_SHAPE;
            case UP -> shortArm ? SHORT_UP_ARM_SHAPE : UP_ARM_SHAPE;
            case NORTH -> shortArm ? SHORT_NORTH_ARM_SHAPE : NORTH_ARM_SHAPE;
            case SOUTH -> shortArm ? SHORT_SOUTH_ARM_SHAPE : SOUTH_ARM_SHAPE;
            case WEST -> shortArm ? SHORT_WEST_ARM_SHAPE : WEST_ARM_SHAPE;
            case EAST -> shortArm ? SHORT_EAST_ARM_SHAPE : EAST_ARM_SHAPE;
        };
    }

    public static VoxelShape[] getArmShapes(boolean shortArm) {
        return Arrays.stream(DIRECTIONS).map((dir) -> getArmShape(dir, shortArm)).toArray(VoxelShape[]::new);
    }

    private PistonFamily family;

    public BasicPistonArmBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(SHORT, false));
    }

    @Override
    public PistonFamily getFamily() {
        return this.family;
    }

    @Override
    public void setFamily(PistonFamily family) {
        this.family = family;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return (state.getValue(SHORT) ? SHORT_ARM_SHAPES : ARM_SHAPES)[state.getValue(FACING).ordinal()];
    }

    public boolean isAttached(BlockState state, BlockState behindState, BlockState frontState) {
        // Must be BasicPistonArmBlock or BasicPistonHeadBlock in front
        if (!frontState.is(this.family.getHead()) && !frontState.is(this)) {
            return false;
        }

        Direction facing = state.getValue(FACING);

        // Head or Arm in front of arm is valid
        if (facing != frontState.getValue(FACING)) {
            return false;
        }

        // If arm is behind, make sure it's a valid arm
        if (behindState.is(this)) {
            return facing == behindState.getValue(FACING);
        }

        // If it's not an arm than it must be a piston base, and a valid one
        return (behindState.is(this.family.getBase(PistonType.DEFAULT)) ||
                behindState.is(this.family.getBase(PistonType.STICKY))) &&
                behindState.getValue(BlockStateProperties.EXTENDED) &&
            facing == behindState.getValue(FACING);
    }

    public void isAttachedOrBreak(Level level, BlockState state, BlockPos behindPos, BlockPos frontPos) {
        boolean validFront, validBack;
        BlockState frontState = level.getBlockState(frontPos);
        // Must be BasicPistonArmBlock or BasicPistonHeadBlock in front
        validFront = (frontState.is(this.family.getHead()) || frontState.is(this)) &&
                (state.getValue(FACING) == frontState.getValue(FACING));
        BlockState backState = level.getBlockState(behindPos);
        if (backState.is(this)) { // If arm is behind, make sure it's a valid arm
            validBack = backState.getValue(FACING) == state.getValue(FACING);
        } else { // If it's not an arm than it must be a piston base, and a valid one
            validBack = (backState.is(this.family.getBase(PistonType.DEFAULT)) ||
                    backState.is(this.family.getBase(PistonType.STICKY))) &&
                    backState.getValue(BlockStateProperties.EXTENDED) &&
                    backState.getValue(FACING) == state.getValue(FACING);
        }
        if (validBack) {
            level.destroyBlock(behindPos, false);
        }
        if (validFront) {
            level.destroyBlock(frontPos, false);
        }
    }

    public boolean isAttachedOrBreak(
            Level world,
            BlockState armState,
            BlockPos backPos,
            BlockPos frontPos,
            BiPredicate<BlockState, BlockState> backOr,
            BiPredicate<BlockState, BlockState> frontOr
    ) {
        BlockState frontState = world.getBlockState(frontPos);
        // Must be BasicPistonArmBlock or BasicPistonHeadBlock in front
        if (frontOr.test(frontState, armState) || ((frontState.is(this.family.getHead()) || frontState.is(this)) &&
                armState.getValue(FACING) == frontState.getValue(FACING))) {
            BlockState backState = world.getBlockState(backPos);
            if (backOr.test(backState, armState)) {
                return true;
            } else if (backState.is(this)) { // If arm is behind, make sure it's a valid arm
                return backState.getValue(FACING) == armState.getValue(FACING);
            } else { // If it's not an arm than it must be a piston base, and a valid one
                return backState.is(this.family.getBase(PistonType.DEFAULT)) ||
                        backState.is(this.family.getBase(PistonType.STICKY)) &&
                                backState.getValue(BlockStateProperties.EXTENDED) &&
                                backState.getValue(FACING) == armState.getValue(FACING);
            }
        }
        return false;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && player.getAbilities().instabuild) {
            Direction facing = state.getValue(FACING);
            this.isAttachedOrBreak(level, state, pos.relative(facing.getOpposite()), pos.relative(facing));
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!newState.is(this)) {
            super.onRemove(state, level, pos, newState, moved);
            if (!moved) {
                Direction facing = state.getValue(FACING);
                this.isAttachedOrBreak(level, state, pos.relative(facing.getOpposite()), pos.relative(facing));
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level,
                                  ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction dir,
                                  BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return dir.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, level, scheduledTickAccess, pos, dir, neighborPos, neighborState, random);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        return this.isAttachedOrBreak((Level)level, state, pos.relative(dir.getOpposite()), pos.relative(dir),
                OR_IS_ATTACHED, OR_IS_ATTACHED);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                @Nullable Orientation orientation, boolean movedByPiston) {
        if (state.canSurvive(level, pos)) {
            BlockPos backPos = pos.relative(state.getValue(FACING).getOpposite());
            level.neighborChanged(level.getBlockState(backPos), backPos, neighborBlock, orientation, false);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean bl) {
        Direction dir = state.getValue(FACING);
        BlockPos nextBlockPos = pos.relative(dir);
        BlockState nextState = level.getBlockState(nextBlockPos);
        while (nextState.is(this)) {
            nextBlockPos = nextBlockPos.relative(dir);
            nextState = level.getBlockState(nextBlockPos);
        }
        return nextState.is(this.family.getHead()) ?
                new ItemStack(this.family.getBase(nextState.getValue(BasicPistonHeadBlock.TYPE))) : ItemStack.EMPTY;
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
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHORT);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return null;
    }
}
