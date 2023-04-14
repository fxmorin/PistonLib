package ca.fxco.pistonlib.blocks.pistons.translocationPiston;

import ca.fxco.pistonlib.blocks.pistons.basePiston.BasicMovingBlockEntity;
import ca.fxco.pistonlib.pistonLogic.families.PistonFamily;

import ca.fxco.pistonlib.pistonLogic.structureGroups.StructureGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TranslocationMovingBlockEntity extends BasicMovingBlockEntity {

    public TranslocationMovingBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public TranslocationMovingBlockEntity(PistonFamily family, StructureGroup group, BlockPos pos, BlockState state,
                                          BlockState movedState, BlockEntity movedBlockEntity, Direction facing,
                                          boolean extending, boolean isSourcePiston) {
        super(family, group, pos, state, movedState, movedBlockEntity, facing, extending, isSourcePiston);
    }

    @Override
    protected AABB moveByPositionAndProgress(BlockPos pos, AABB aabb) {
        return super.moveByPositionAndProgress(pos, aabb).inflate(0.01D); // Cheating ;)
    }

    @Override
    protected void moveEntity(Direction noclipDir, Entity entity, double amount, Direction moveDir) {
        amount += 0.1D;
        entity.move(MoverType.SELF, new Vec3(
                amount * moveDir.getStepX(),
                amount * moveDir.getStepY(),
                amount * moveDir.getStepZ()
        ));
    }
}
