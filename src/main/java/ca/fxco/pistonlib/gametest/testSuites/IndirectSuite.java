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
}
