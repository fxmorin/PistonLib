package ca.fxco.pistonlib.gametest;

import ca.fxco.api.gametestlib.config.IndirectParsedValue;
import ca.fxco.api.gametestlib.config.binder.ConfigBinder;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.config.ParsedValue;

import java.util.HashMap;
import java.util.Map;

public class PistonLibConfigBinder implements ConfigBinder {
    @Override
    public Map<String, ca.fxco.api.gametestlib.config.ParsedValue<?>> registerConfigValues() {
        Map<String, ca.fxco.api.gametestlib.config.ParsedValue<?>> configValues = new HashMap<>();
        for (Map.Entry<String, ParsedValue<?>> entry : PistonLib.getConfigManager().getParsedValues().entrySet()) {
            configValues.put(entry.getKey(), convertToParsedValue(entry.getValue()));
        }
        return configValues;
    }

    private <T> ca.fxco.api.gametestlib.config.ParsedValue<T> convertToParsedValue(ParsedValue<T> value) {
        return new IndirectParsedValue<>(value::setValue, value::reset, value::getAllTestingValues);
    }
}
