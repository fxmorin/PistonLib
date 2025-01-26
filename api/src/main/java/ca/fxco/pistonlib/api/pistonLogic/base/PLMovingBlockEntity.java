package ca.fxco.pistonlib.api.pistonLogic.base;

import ca.fxco.pistonlib.api.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.api.pistonLogic.sticky.StickyType;
import ca.fxco.pistonlib.api.pistonLogic.structure.StructureGroup;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//TODO-API: JavaDoc
public interface PLMovingBlockEntity {

    PistonFamily getFamily();

    @Nullable StructureGroup getStructureGroup();

    void setStructureGroup(@Nullable StructureGroup structureGroup);

    boolean hasControl();

    float speed();

    BlockState getStateForMovingEntities();

    void finalTickStuckNeighbors(Map<Direction, StickyType> stickyTypes);

    void finalTick(boolean skipStickiness, boolean removeSource);

    void tick();

    void onMovingTick(Direction movingDirection, float speed);
}
