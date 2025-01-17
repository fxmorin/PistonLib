package ca.fxco.pistonlib.mixin.stuck;

import ca.fxco.api.pistonlib.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.PistonLibConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.level.block.ChestBlock.*;

@Mixin(ChestBlock.class)
public abstract class ChestBlock_stuckMixin extends Block {

    public ChestBlock_stuckMixin(Properties properties) {
        super(properties);
    }

    private static Optional<Direction> getStickyDirection(BlockState state) {
        ChestType type = state.getValue(TYPE);
        if (type == ChestType.SINGLE) {
            return Optional.empty();
        }
        Direction dir = state.getValue(FACING);
        return Optional.of(type == ChestType.LEFT ? dir.getClockWise() : dir.getCounterClockWise());
    }

    @Override
    public boolean pl$usesConfigurablePistonStickiness() {
        return PistonLibConfig.stuckDoubleBlocks;
    }

    @Override
    public Map<Direction, StickyType> pl$stickySides(BlockState state) {
        return getStickyDirection(state).map(dir -> Map.of(dir, StickyType.CONDITIONAL)).orElseGet(Map::of);
    }

    @Override
    public StickyType pl$sideStickiness(BlockState state, Direction dir) {
        Optional<Direction> dirOpt = getStickyDirection(state);
        return dirOpt.isPresent() && dirOpt.get() == dir ? StickyType.CONDITIONAL : StickyType.DEFAULT;
    }

    @Override
    public boolean pl$matchesStickyConditions(BlockState state, BlockState neighborState, Direction dir) {
        if (state.is(neighborState.getBlock())) {
            Optional<Direction> dirOpt = getStickyDirection(neighborState);
            return dirOpt.isPresent() && dirOpt.get() == dir.getOpposite();
        }
        return false;
    }

    @Override
    public boolean pl$propagatesIndirectSticky(BlockState state) {
        return false;
    }
}
