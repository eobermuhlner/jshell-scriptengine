package ch.obermuhlner.scriptengine.jshell;

import java.util.Map;

public class VariablesTransfer {
    private static final ThreadLocal<Map<String, Object>> threadLocalVariables = new ThreadLocal<>();

    private VariablesTransfer() {
        // empty
    }

    public static void setVariables(Map<String, Object> variables) {
        threadLocalVariables.set(variables);
    }

    public static Object getVariableValue(String name) {
        return threadLocalVariables.get().get(name);
    }

    public static void setVariableValue(String name, Object value) {
        threadLocalVariables.get().put(name, value);
    }

    public static void clear() {
        threadLocalVariables.get().clear();
    }

}
