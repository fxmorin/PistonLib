package ca.fxco.pistonlib.api.pistonLogic.families;

/**
 * A member of a piston family.
 * 
 * @author Space Walker
 * @since 1.2.0
 */
public interface PistonFamilyMember {

    /**
     * @return the piston family this member belongs to.
     * @since 1.2.0
     */
    PistonFamily getFamily();

    /**
     * Assigns a piston family to this member.
     *
     * @param family the piston family this member should be assigned to.
     * @since 1.2.0
     */
    void setFamily(PistonFamily family);

}
