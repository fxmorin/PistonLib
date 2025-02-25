package ca.fxco.pistonlib.gametest.testSuites;

import ca.fxco.api.gametestlib.gametest.Change;
import ca.fxco.api.gametestlib.gametest.Config;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.api.gametestlib.gametest.GameTestLib;
import ca.fxco.gametestlib.gametest.GameTestUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class BasicTestSuite {

    // Make sure pistons can push 12 blocks
    @GameTest(timeoutTicks = 4)
    public void push12(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Make sure pistons cant push 13 blocks
    @GameTest(timeoutTicks = 4)
    public void pushLimit(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Tests the piston pushing 12 blocks than only pulling 11 back
    @GameTest(timeoutTicks = 7)
    public void pushAndDrop(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Make sure Honey doesn't stick to Slime
    @GameTest(timeoutTicks = 5)
    public void slimeAndHoney(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Make sure obsidian is still immovable
    @GameTest(timeoutTicks = 4)
    public void immovable(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Check if 2 game tick pulses still keep waterlogged state
    @GameTestLib(
            variants = "pistonsPushWaterloggedBlocks",
            config = @Config(
                    name = "pistonsPushWaterloggedBlocks",
                    changes = @Change(value = "NONE", change = GameTestChanges.FLIP_ALL)
            )
    )
    @GameTest(timeoutTicks = 5)
    public void waterPushDirectly(GameTestHelper helper) {}

    // Make sure 2 game tick pulses only push water when directly in front of the piston
    @GameTestLib(
            variants = "pistonsPushWaterloggedBlocks",
            config = @Config(
                    name = "pistonsPushWaterloggedBlocks",
                    changes = @Change(value = "ALL", change = GameTestChanges.FLIP_ALL)
            )
    )
    @GameTest(timeoutTicks = 5)
    public void waterPushInDirectly(GameTestHelper helper) {}

    // Piston should break bedrock if headless
    @GameTestLib(
            variants = "illegalBreakingFix",
            config = @Config(
                    name = "illegalBreakingFix",
                    changes = @Change(value = "true", change = GameTestChanges.FLIP_ALL)
            ),
            inverted = "headlessPistonFix"
    )
    @GameTest(timeoutTicks = 4)
    public void headlessPistonIllegalBreak(GameTestHelper helper) {}

    // Check if the strong piston can still pull 24 blocks
    @GameTest(timeoutTicks = 90)
    public void strongPull(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Check if the strong piston can still push 24 blocks
    @GameTest(timeoutTicks = 90)
    public void strongPush(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Make sure 0-ticks still work with!
    @GameTest(timeoutTicks = 6)
    public void zerotick(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Check if a retracting sticky piston head is still missing an update if it fails to pull a structure
    @GameTest(timeoutTicks = 7)
    public void headRetractionUpdate(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Check half slime block side behavior
    @GameTest(timeoutTicks = 4)
    public void halfSlimeSides(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }

    // Check half slime and half honey interaction
    @GameTest(timeoutTicks = 4)
    public void halfSlimeHoney(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }
}
