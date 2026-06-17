package environment;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> variables = new LinkedHashMap<>();

    public void set(String name, Object value) {
        variables.put(name, value);
    }

    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException(
                "Runtime error: variable not defined: '" + name + "'. "
                + "Did you forget to assign it with 'put ... into " + name + "'?");
        }
        return variables.get(name);
    }

    public boolean isDefined(String name) {
        return variables.containsKey(name);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(variables);
    }
}
