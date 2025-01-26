package ca.fxco.pistonlib.gametest;

import ca.fxco.api.gametestlib.config.IndirectResolvedValue;
import ca.fxco.api.gametestlib.config.ResolvedValue;
import ca.fxco.api.gametestlib.config.binder.ConfigBinder;
import ca.fxco.pistonlib.PistonLib;
import ca.fxco.pistonlib.api.config.ParsedValue;

import java.util.HashMap;
import java.util.Map;

public class PistonLibConfigBinder implements ConfigBinder {
    @Override
    public Map<String, ResolvedValue<?>> registerConfigValues() {
        Map<String, ResolvedValue<?>> configValues = new HashMap<>();
        for (ParsedValue<?> value : PistonLib.getConfigManager().getParsedValues()) {
            configValues.put(value.getName(), convertToParsedValue(value));
        }
        return configValues;
    }

    private <T> ResolvedValue<T> convertToParsedValue(ParsedValue<T> value) {
        return new IndirectResolvedValue<>(value::setValue, value::reset, value::getAllTestingValues);
    }
}
