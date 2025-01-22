package ca.fxco.pistonlib.blocks.pistons.basePiston;

import ca.fxco.api.pistonlib.block.PLPistonController;
import ca.fxco.api.pistonlib.pistonLogic.controller.PistonController;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BasicPistonBaseBlock extends DirectionalBlock implements PLPistonController {

    private static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
    private static final VoxelShape EXTENDED_EAST_SHAPE = Block.box(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);
    private static final VoxelShape EXTENDED_WEST_SHAPE = Block.box(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape EXTENDED_SOUTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);
    private static final VoxelShape EXTENDED_NORTH_SHAPE = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);
    private static final VoxelShape EXTENDED_UP_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    private static final VoxelShape EXTENDED_DOWN_SHAPE = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);

    private final PistonController controller;

    public BasicPistonBaseBlock(PistonController controller) {
        this(controller, FabricBlockSettings.copyOf(Blocks.PISTON));
    }

    public BasicPistonBaseBlock(PistonController controller, Properties properties) {
        super(properties);

        this.controller = controller;
        this.controller.setBase(this);

        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(EXTENDED, false)
        );
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return null; // codec isn't used now. currently it's just a preparation for data driven blocks
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (state.getValue(EXTENDED)) {
            return switch (state.getValue(FACING)) {
                case UP    -> EXTENDED_UP_SHAPE;
                case DOWN  -> EXTENDED_DOWN_SHAPE;
                case NORTH -> EXTENDED_NORTH_SHAPE;
                case SOUTH -> EXTENDED_SOUTH_SHAPE;
                case EAST  -> EXTENDED_EAST_SHAPE;
                case WEST  -> EXTENDED_WEST_SHAPE;
            };
        }

        return Shapes.block();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        this.controller.checkIfExtend(level, pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        this.controller.checkIfExtend(level, pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(this) && level.getBlockEntity(pos) == null) {
            this.controller.checkIfExtend(level, pos, state);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
            .setValue(FACING, ctx.getNearestLookingDirection().getOpposite())
            .setValue(EXTENDED, false);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data) {
        return this.controller.triggerEvent(state, level, pos, type, data);
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
        builder.add(FACING, EXTENDED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(EXTENDED);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public PistonController pl$getPistonController() {
        return this.controller;
    }

}
