package ca.fxco.pistonlib.api.pistonLogic.base;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.api.pistonLogic.structureGroups.StructureGroup;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * PistonLib's Moving Block Entity interface.
 * This is the base of all moving block entities used by PistonLib
 *
 * @author FX
 * @since 1.2.0
 */
public interface PLMovingBlockEntity {

    /**
     * Gets the family of this moving block entity
     *
     * @return the piston family this moving block entity belongs to.
     * @since 1.2.0
     */
    PistonFamily getFamily();

    /**
     * Gets the structure group of this moving block entity
     *
     * @return the structure group this moving block entity belongs to,
     *  {@code null} if this moving block entity doesn't belong to a structure group
     * @since 1.2.0
     */
    @Nullable StructureGroup getStructureGroup();

    /**
     * Sets the structure group that this moving block entity belongs to
     *
     * @param structureGroup The structure group that this moving block entity is a part of
     * @since 1.2.0
     */
    void setStructureGroup(@Nullable StructureGroup structureGroup);

    /**
     * If this block entity is in control of the structure group that it's a part of
     *
     * @return {@code false} if it's not part of a group, or it's not in control. Otherwise {@code true}
     * @since 1.2.0
     */
    boolean hasControl();

    /**
     * The speed that this moving block entity is moving at
     *
     * @return The speed that the block entity is moving at
     * @since 1.2.0
     */
    float speed();

    /**
     * Gets the block state used for collisions.
     * This uses the short piston head collision when needed
     * to prevent pushing entity behind the piston.
     *
     * @return The blockstate used for moving entities
     * @since 1.2.0
     */
    BlockState getStateForMovingEntities();

    /**
     * Called at the end of {@link #finalTick}.
     * It makes it so that neighboring moving block entities which have a strong sticky connection,
     * block drop together.
     *
     * @param stickyTypes The sticky types of the block being moved
     * @since 1.2.0
     */
    void finalTickStuckNeighbors(Map<Direction, StickyType> stickyTypes);

    /**
     * Called during the last tick before the moving block entity attempts to place itself.
     *
     * @param skipStickiness If sticky behavior should be ignored
     * @param removeSource   If the source block should be removed
     * @since 1.2.0
     */
    void finalTick(boolean skipStickiness, boolean removeSource);

    /**
     * Called when using the ticking API.
     * Allows moving block entities to be ticked while moving.
     *
     * @param movingDirection The direction that the moving block entity is moving
     * @param speed           The speed at which its moving
     * @since 1.2.0
     */
    void onMovingTick(Direction movingDirection, float speed);
}
