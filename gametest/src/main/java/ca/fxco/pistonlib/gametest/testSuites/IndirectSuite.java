package ca.fxco.pistonlib.gametest.testSuites;

import ca.fxco.api.gametestlib.gametest.Config;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.api.gametestlib.gametest.GameTestLib;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

//@GameTestLib("indirectStickyApi")
public class IndirectSuite {

    // Make sure that the honey block is pulled along when using the indirect api
    @GameTestLib(
            value = "allStickyTypesAreIndirect",
            config = @Config(value = "false", changes = GameTestChanges.FLIP_ALL, optionName = "allStickyTypesAreIndirect")
    )
    @GameTest(timeoutTicks = 4)
    public void side1(GameTestHelper helper) {}

    // Chests shouldn't pull beds when using indirectStickyApi. https://github.com/FxMorin/PistonLib/pull/48
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void doubleblocks1(GameTestHelper helper) {}

    // Chests shouldn't be pulled through blocks when using indirectStickyApi. https://github.com/FxMorin/PistonLib/pull/48
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void doubleblocks2(GameTestHelper helper) {}

    // Glue blocks shouldn't move stickyless blocks
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void stickyless1(GameTestHelper helper) {}

    // Piston shouldn't pull stickyless blocks right infront of it
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void stickyless2(GameTestHelper helper) {}

    // Piston shouldn't pull half_obsidian blocks right infront of it.
    // This is different from stickyless2 to check if the direction is correct
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void stickyless3(GameTestHelper helper) {}

    // Check if having a slime block inbetween can change the outcome
    @GameTestLib("allStickyTypesAreIndirect")
    @GameTest(timeoutTicks = 4)
    public void stickyless4(GameTestHelper helper) {}

}
