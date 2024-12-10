package ca.fxco.pistonlib;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface CustomParsedValues {

    /**
     * Used to add custom parsedValues to other mod's configManager
     * @return map with modid of the mod as a key and list of parsedValues to add as a value
     */
    Map<String, List<Field>> getParsedValue();

}
