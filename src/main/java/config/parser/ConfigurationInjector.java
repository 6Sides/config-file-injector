package config.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Injects the parsed config values into the code
 */
public class ConfigurationInjector {

    private ConfigParser parser;

    public static void withApplication(String application, String basePackage) {
        new ConfigurationInjector(new ConfigParser(application)).inject(basePackage);
    }

    private ConfigurationInjector(ConfigParser parser) {
        this.parser = parser;
    }

    public void inject(String basePackage) {
        PackageScanner scanner = new PackageScanner(basePackage);

        try {
            for (Class<?> clazz : scanner.getClasses()) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ConfigValue.class) && Modifier
                            .isStatic(field.getModifiers())) {

                        String key = field.getAnnotation(ConfigValue.class).value();

                        field.setAccessible(true);

                        Class<?> fieldType = field.getType();
                        Object value;


                        if (fieldType == Integer.class || fieldType == int.class) {
                            value = parser.getInt(key);
                        } else if (fieldType == Double.class || fieldType == double.class) {
                            value = parser.getDouble(key);
                        } else if (fieldType == Float.class || fieldType == float.class) {
                            value = parser.getFloat(key);
                        } else if (fieldType == Short.class || fieldType == short.class) {
                            value = parser.getShort(key);
                        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                            value = parser.getBool(key);
                        } else {
                            value = parser.getString(key);
                        }

                        field.set(null, value);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
