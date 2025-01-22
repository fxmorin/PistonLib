package ca.fxco.api.pistonlib.pistonLogic.controller;

import ca.fxco.api.pistonlib.pistonLogic.structure.StructureRunner;
import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.pistonLogic.structureResolvers.BasicStructureResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * An interface used as the base of all piston related calls from blocks.
 * Having this logic split from the physical blocks allows any block to gain the ability to become a piston.
 *
 * @author FX
 * @since 1.0.4
 */
public interface PistonController {

    /**
     * Gets the piston family used by this controller.
     *
     * @return The piston family for this controller.
     */
    PistonFamily getFamily();

    /**
     * Gets the piston type used by this controller.
     *
     * @return The piston type for this controller.
     */
    PistonType getType();

    /**
     * Sets the base block for the piston family.
     * Should call {@link PistonFamily#setBase}
     *
     * @param block The block linked to this controller.
     */
    default void setBase(Block block) {
        getFamily().setBase(getType(), block);
    }

    /**
     * Creates a new structure resolver
     *
     * @param level  The level to run the structure runner in
     * @param pos    The position of the piston calling the runner
     * @param facing The facing direction of the piston calling the runner
     * @param length The length that the piston calling the runner will attempt to reach
     * @param extend If this should be an extension or retraction
     * @return The new structure resolver
     */
    BasicStructureResolver newStructureResolver(Level level, BlockPos pos, Direction facing,
                                                int length, boolean extend);

    /**
     * Creates a new structure runner
     *
     * @param level             The level to run the structure runner in
     * @param pos               The position of the piston calling the runner
     * @param facing            The facing direction of the piston calling the runner
     * @param length            The length that the piston calling the runner will attempt to reach
     * @param extend            If this should be an extension or retraction
     * @param structureProvider A structure resolver provider
     * @return The new structure runner
     */
    StructureRunner newStructureRunner(
            Level level, BlockPos pos, Direction facing, int length, boolean extend,
            BasicStructureResolver.Factory<? extends BasicStructureResolver> structureProvider
    );

    /**
     * When extending, it gets the maximum length of the piston.
     * When retracting, it gets the minimum length of the piston.
     *
     * @return the length that the piston should have once it's done moving blocks
     */
    int getLength(Level level, BlockPos pos, BlockState state);

    /**
     * Used to check if piston is powered
     *
     * @param level of the block state
     * @param pos block position of the block state
     * @param facing direction to not check for the signal, usually piston facing
     * @return {@code true} if block states neighbors has signal except from one direction
     */
    boolean hasNeighborSignal(Level level, BlockPos pos, Direction facing);

    /**
     * Checks if piston should extend and then adds block event based on that
     *
     * @param level of the block state
     * @param pos block position of the block state
     * @param state block state to check
     */
    void checkIfExtend(Level level, BlockPos pos, BlockState state);

    /**
     * What should it do on retract, nothing, retract and pull blocks or just retract without pulling
     *
     * @param level of the block state
     * @param pos block position of the block state
     * @param facing the direction piston extends in
     * @param length of the piston
     * @return retract type of the block state
     */
    int getRetractType(ServerLevel level, BlockPos pos, Direction facing, int length);

    /**
     * Vanilla method that we also implement
     *
     * @param state block state of the piston base
     * @param level of the block state
     * @param pos block position of the block state
     * @param type retract type {@link ca.fxco.api.pistonlib.pistonLogic.PistonEvents}
     * @param data 3D data of the direction
     * @return {@code false} if event failed
     */
    boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data);

    void playEvents(Level level, Holder<GameEvent> event, BlockPos pos);

    /**
     * Returns whether this piston can move the given block,
     * taking into account world height/border as well as
     * vanilla and custom piston push reactions.
     *
     * @param state        The state we are attempting to move
     * @param level        The level in which we are attempting to move the block
     * @param pos          The position of the block we are attempting to move
     * @param moveDir      The direction we are attempting to move the block in
     * @param allowDestroy If we are allowed to destroy blocks
     * @param pistonFacing The direction that the piston moving this block is facing
     */
    boolean canMoveBlock(BlockState state, Level level, BlockPos pos, Direction moveDir,
                         boolean allowDestroy, Direction pistonFacing);

    /**
     * Returns whether this piston controller can move the given block.
     * This method assumes the world height/border and push reaction
     * checks have all succeeded.
     *
     * @param state The block state that we are attempting to move
     */
    boolean canMoveBlock(BlockState state);

    /**
     * The method used to move blocks from a position.
     * For normal pistons, you would usually create your structure runner here.
     *
     * @param level  The level to move the block in
     * @param pos    The position of the first block to be moved
     * @param facing The direction to move the blocks in
     * @param length The length that the piston should have once it's done moving blocks
     * @param extend If this should be an extension or retraction
     * @return {@code true} if the controller was able to move the blocks, otherwise {@code false}
     */
    boolean moveBlocks(Level level, BlockPos pos, Direction facing, int length, boolean extend);
}
