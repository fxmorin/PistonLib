package ca.fxco.pistonlib.pistonLogic.structureGroups;

import ca.fxco.pistonlib.api.pistonLogic.base.PLMovingBlockEntity;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.helpers.NbtUtils;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Consumer;

@NoArgsConstructor
public class ServerStructureGroup implements StructureGroup {

    // The order they are added to the group is the order they should run in
    private final List<PistonMovingBlockEntity> blockEntities = new ArrayList<>();

    @Override
    public boolean hasInitialized() {
        return true;
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(P blockEntity) {
        this.blockEntities.add(blockEntity);
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void add(int index, P blockEntity) {
        this.blockEntities.add(index, blockEntity);
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void remove(P blockEntity) {
        this.blockEntities.remove(blockEntity);
    }

    @Override
    public void remove(int index) {
        this.blockEntities.remove(index);
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> P get(int index) {
        //noinspection unchecked
        return (P) this.blockEntities.get(index);
    }

    @Override
    public int size() {
        return this.blockEntities.size();
    }

    @Override
    public BlockState getState(BlockPos blockPos) {
        throw new UnsupportedOperationException("This can only be called from the client!");
    }

    //
    // Grouped methods
    //

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forEach(Consumer<P> action) {
        this.blockEntities.forEach(be -> {

        });
    }

    @Override
    public <P extends PistonMovingBlockEntity & PLMovingBlockEntity> void forNonControllers(Consumer<P> action) {
        for (int i = 1; i < this.blockEntities.size(); i++) {
            //noinspection unchecked
            action.accept((P) this.blockEntities.get(i));
        }
    }

    //
    // Saving / Loading
    //

    // Attempt to find other block entities from your structure
    @Override
    public void load(Level level, List<BlockPos> blockPosList) {
        for (BlockPos blockPos : blockPosList) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof BasicMovingBlockEntity bmbe) {
                this.blockEntities.add(bmbe);
                bmbe.setStructureGroup(this);
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        this.blockEntities.removeIf(be -> be.isRemoved() || !be.hasLevel());
        if (this.blockEntities.isEmpty()) {
            return;
        }
        PistonMovingBlockEntity controller = this.blockEntities.getFirst();
        NbtUtils.saveCompactRelativeBlockPosList(
                nbt,
                "controller",
                controller.getBlockPos(),
                i -> this.blockEntities.get(i + 1).getBlockPos(),
                this.blockEntities.size() - 1,
                ((PLMovingBlockEntity)controller).getFamily().getPushLimit()
        );
    }
}
