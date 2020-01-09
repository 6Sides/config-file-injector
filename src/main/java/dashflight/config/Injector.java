package dashflight.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Injects the parsed config values into the code
 */
public class Injector {

    private ConfigParser parser;

    public Injector(String configFile) throws IOException {
        parser = new ConfigParser(configFile);
    }

    public void inject(String basePackage) throws YamlParsingException {
        PackageScanner scanner = new PackageScanner(basePackage);

        try {
            for (Class<?> clazz : scanner.getClasses()) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ConfigValue.class) && Modifier
                            .isStatic(field.getModifiers())) {

                        String key = field.getAnnotation(ConfigValue.class).value();

                        field.setAccessible(true);

                        Object value = parser.getObject(key);

                        if (field.getType() == String.class) {
                            field.set(null, value.toString());
                            continue;
                        }

                        if (value instanceof Integer) {
                            field.set(null, ((Integer) value).intValue());
                        } else if (value instanceof Double) {
                            field.set(null, ((Double) value).doubleValue());
                        } else if (value instanceof Float) {
                            field.set(null, ((Float) value).floatValue());
                        } else if (value instanceof Boolean) {
                            field.set(null, ((Boolean) value).booleanValue());
                        } else {
                            field.set(null, field.getType().cast(value));
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new YamlParsingException();
        }
    }

}
