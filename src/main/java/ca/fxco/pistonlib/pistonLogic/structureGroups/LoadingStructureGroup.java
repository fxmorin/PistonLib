package ca.fxco.pistonlib.pistonLogic.structureGroups;

import ca.fxco.pistonlib.api.pistonLogic.base.PLMovingBlockEntity;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import ca.fxco.pistonlib.helpers.NbtUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This structure group temporarily holds a blockPos list till the block entity ticks for the first time,
 * and sets the real structure group.
 *
 * @author FX
 */
@Getter
@AllArgsConstructor
public class LoadingStructureGroup implements StructureGroup {

    private final List<BlockPos> blockPosList = new ArrayList<>();

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(P blockEntity) {}

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(int index, P blockEntity) {}

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void remove(P blockEntity) {}

    @Override
    public void remove(int index) {}

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> P get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public BlockState getState(BlockPos blockPos) {
        return null;
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forEach(Consumer<P> action) {}

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forNonControllers(Consumer<P> action) {}

    @Override
    public void load(Level level, List<BlockPos> blockPosList) {}

    @Override
    public void saveAdditional(CompoundTag nbt) {}

    public void onLoad(CompoundTag nbt, BlockPos basePos, int pushLimit) {
        if (!nbt.contains("controller")) {
            return;
        }

        blockPosList.addAll(NbtUtils.loadCompactRelativeBlockPosList(nbt, "controller", basePos, pushLimit));
    }
}
