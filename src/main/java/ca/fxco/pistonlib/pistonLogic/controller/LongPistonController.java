package ca.fxco.pistonlib.pistonLogic.controller;

import ca.fxco.pistonlib.helpers.Utils;
import ca.fxco.pistonlib.pistonLogic.families.PistonFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.EXTENDED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class LongPistonController extends VanillaPistonController {

    public LongPistonController(PistonFamily family, PistonType type) {
        super(family, type);
    }

    @Override
    public boolean hasNeighborSignal(Level level, BlockPos pos, Direction facing) {
        return Utils.hasNeighborSignalExceptFromFacing(level, pos, facing) ||
                (this.getFamily().isQuasi() && level.pl$hasQuasiNeighborSignal(pos, 1));
    }

    @Override
    public int getLength(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(EXTENDED)) {
            Direction facing = state.getValue(FACING);
            int length = this.getFamily().getMinLength();

            while (length++ < this.getFamily().getMaxLength()) {
                BlockPos frontPos = pos.relative(facing, length);
                BlockState frontState = level.getBlockState(frontPos);

                if (!frontState.is(this.getFamily().getArm())) {
                    break;
                }
            }

            return length;
        }
        return this.getFamily().getMinLength();
    }
}
