package ca.fxco.pistonlib.base;

import ca.fxco.api.pistonlib.pistonLogic.families.PistonBehavior;
import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamilies;
import ca.fxco.api.pistonlib.pistonLogic.families.PistonFamily;
import ca.fxco.pistonlib.pistonLogic.families.ModPistonFamily;

import java.util.Objects;

import static ca.fxco.pistonlib.PistonLib.id;

public class ModPistonFamilies {

    public static final PistonFamily BASIC = register("basic", ModPistonFamily.of(PistonBehavior.DEFAULT, false));
    public static final PistonFamily LONG = register("long", ModPistonFamily.of(PistonBehavior.builder().maxLength(12).noQuasi().build(), false));
    public static final PistonFamily CONFIGURABLE = register("configurable", ModPistonFamily.of(PistonBehavior.builder().maxLength(2).noQuasi().extendingSpeed(0.1F).retractingSpeed(0.5F).build(), false));
    public static final PistonFamily STALE = register("stale", ModPistonFamily.of(PistonBehavior.builder().noQuasi().build(), false));
    public static final PistonFamily VERY_QUASI = register("very_quasi", ModPistonFamily.of(PistonBehavior.DEFAULT, false));
    public static final PistonFamily STRONG = register("strong", ModPistonFamily.of(PistonBehavior.builder().speed(0.05F).pushLimit(24).build()));
    public static final PistonFamily FAST = register("fast", ModPistonFamily.of(PistonBehavior.builder().pushLimit(2).build(), false));
    public static final PistonFamily FRONT_POWERED = register("front_powered", ModPistonFamily.of(PistonBehavior.builder().frontPowered().build(), false));
    public static final PistonFamily SLIPPERY = register("slippery", ModPistonFamily.of(PistonBehavior.DEFAULT, false));
    public static final PistonFamily SUPER = register("super", ModPistonFamily.of(PistonBehavior.builder().pushLimit(Integer.MAX_VALUE).verySticky().build(), false));
    public static final PistonFamily MBE = register("mbe", ModPistonFamily.of(PistonBehavior.DEFAULT, false));
    public static final PistonFamily VERY_STICKY = register("very_sticky", ModPistonFamily.of(PistonBehavior.builder().verySticky().build(), false));

    private static PistonFamily register(String name, PistonFamily family) {
        return PistonFamilies.register(id(name), family);
    }

    public static void bootstrap() { }

    private static boolean locked;

    public static boolean requireNotLocked() {
        if (locked) {
            throw new IllegalStateException("cannot alter piston families after they have been locked!");
        }

        return true;
    }

    public static void validate() {
        if (!locked) {
            ModRegistries.PISTON_FAMILY.forEach(family -> {
                try {
                    if (family.getBases().isEmpty())
                        throw new IllegalStateException("missing base block");
                    Objects.requireNonNull(family.getHead(), "head block");
                    if (family.getMaxLength() > 1)
                        Objects.requireNonNull(family.getArm(), "missing arm block");
                    Objects.requireNonNull(family.getMoving(), "moving block");
                    Objects.requireNonNull(family.getMovingBlockEntityType(), "moving block entity type");
                    Objects.requireNonNull(family.getMovingBlockEntityFactory(), "moving block entity factory");
                } catch (Exception e) {
                    throw new IllegalStateException("piston family " + family + " is invalid!", e);
                }
            });

            locked = true;
        }
    }
}
