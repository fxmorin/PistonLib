package ca.fxco.pistonlib.api.pistonLogic.structureGroups;

import ca.fxco.pistonlib.api.pistonLogic.base.PLMovingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class ServerStructureGroup implements StructureGroup {

    // The order they're added to the group is the order they should run in
    private final List<PistonMovingBlockEntity> blockEntities = new ArrayList<>();

    public ServerStructureGroup() {}

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
            if (be instanceof PistonMovingBlockEntity pmbe) {
                this.blockEntities.add(pmbe);
                if (pmbe instanceof PLMovingBlockEntity plmbe) {
                    plmbe.setStructureGroup(this);
                }
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
        saveCompactRelativeBlockPosList(
                nbt,
                "controller",
                controller.getBlockPos(),
                i -> this.blockEntities.get(i + 1).getBlockPos(),
                this.blockEntities.size() - 1,
                ((PLMovingBlockEntity)controller).getFamily().getPushLimit()
        );
    }

    /**
     * Saves a list of compact {@link BlockPos} relative to a base position to NBT
     *
     * @param nbt The {@link CompoundTag} to save the compact relative {@link BlockPos} list to
     * @param id The identifier to use when saving the compact relative {@link BlockPos} list to the {@link CompoundTag}
     * @param basePos The base position to use when calculating the relative positions
     * @param blockPosFunction The function to retrieve the relative {@link BlockPos} to save for each index
     * @param amt The amount of relative {@link BlockPos} to save
     * @param max The maximum value that any relative {@link BlockPos} in the list can have
     */
    public static void saveCompactRelativeBlockPosList(CompoundTag nbt, String id, BlockPos basePos,
                                                       IntFunction<BlockPos> blockPosFunction, int amt, int max) {
        if (max <= 126) { // block positions fit within 3 bytes
            byte[] positions = new byte[amt * 3];
            int bytePos = 0;
            for (int i = 0; i < amt; i++) {
                BlockPos pos = blockPosFunction.apply(i);
                positions[bytePos++] = (byte)(pos.getX() - basePos.getX());
                positions[bytePos++] = (byte)(pos.getY() - basePos.getY());
                positions[bytePos++] = (byte)(pos.getZ() - basePos.getZ());
            }
            nbt.putByteArray(id, positions);
        } else { // just use ints
            int[] positions = new int[amt * 3];
            int intPos = 0;
            for (int i = 0; i < amt; i++) {
                BlockPos pos = blockPosFunction.apply(i);
                positions[intPos++] = pos.getX() - basePos.getX();
                positions[intPos++] = pos.getY() - basePos.getY();
                positions[intPos++] = pos.getZ() - basePos.getZ();
            }
            nbt.putIntArray(id, positions);
        }
    }
}
