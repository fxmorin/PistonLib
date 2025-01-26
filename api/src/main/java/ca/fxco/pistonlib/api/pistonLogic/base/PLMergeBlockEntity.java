package ca.fxco.pistonlib.api.pistonLogic.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

//TODO-API: JavaDoc
public interface PLMergeBlockEntity {

    Map<Direction, MergeData> getMergingBlocks();

    // Should always be called before calling `canMerge()`
    boolean canMergeFromSide(Direction pushDirection);

    boolean canMerge(BlockState state, Direction dir);

    void doMerge(BlockState state, Direction dir);

    void doMerge(BlockState state, Direction dir, float speed);

    void doMerge(BlockState state, BlockEntity blockEntity, Direction dir, float speed);

    float getXOff(Direction dir, float f, float progress, float lastProgress);

    float getYOff(Direction dir, float f, float progress, float lastProgress);

    float getZOff(Direction dir, float f, float progress, float lastProgress);


    interface MergeData {

        boolean hasBlockEntity();

        BlockEntity getBlockEntity();

        BlockState getState();

        float getProgress();

        float getLastProgress();

        float getSpeed();

        void setProgress(float progress);

        void setLastProgress(float lastProgress);

        void setAllProgress(float progress);

        void setSpeed(float speed);

        void onMovingTick(Level level, BlockPos toPos, Direction dir);
    }
}
