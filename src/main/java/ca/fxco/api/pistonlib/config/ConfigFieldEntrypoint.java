package ca.fxco.api.pistonlib.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * use this if you want to add/override others mod's config fields
 * @author Foxy
 * @since 1.0.4
 */
public interface ConfigFieldEntrypoint {

    /**
     * Used to add custom parsedValues to other mod's configManager
     * @return map with modid of the mod as a key and list of parsedValues to add as a value
     * @since 1.0.4
     */
    Map<String, List<Field>> getConfigFields();

}
