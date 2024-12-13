package ca.fxco.api.pistonlib.blockEntity;

/**
 * If a block entity implements this interface. They will be run once, before there first tick
 * @author FX
 * @since 1.0.4
 */
public interface BlockEntityPostLoad {

    /**
     * @return true if block entity should be added to post load
     * @since 1.0.4
     */
    boolean pl$shouldPostLoad();

    /**
     * This only gets fired once, before your first tick!
     * Be very very careful with what you do in here!
     * @since 1.0.4
     */
    void pl$onPostLoad();

}
