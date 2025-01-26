package ca.fxco.pistonlib.api.pistonLogic.structureGroups;

import ca.fxco.pistonlib.api.pistonLogic.base.PLMovingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ClientStructureGroup extends ServerStructureGroup {

    private final Map<BlockPos, BlockState> fastStateLookup = new HashMap<>();

    public ClientStructureGroup() {}

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(P blockEntity) {
        super.add(blockEntity);
        fastStateLookup.put(blockEntity.getBlockPos()
                .relative(blockEntity.getMovementDirection().getOpposite()), blockEntity.getMovedState());
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(int index, P blockEntity) {
        super.add(index, blockEntity);
        fastStateLookup.put(blockEntity.getBlockPos()
                .relative(blockEntity.getMovementDirection().getOpposite()), blockEntity.getMovedState());
    }

    @Override
    public BlockState getState(BlockPos blockPos) {
        BlockState state = fastStateLookup.get(blockPos);
        return state == null ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
