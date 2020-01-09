package config.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Injects the parsed config values into the code
 */
public class Injector {

    private ConfigParser parser;

    public static Injector withRemoteFile(String fileUrl) throws IOException {
        return new Injector(ConfigParser.withRemoteFile(fileUrl));
    }

    public static Injector withLocalFile(String filePath) throws IOException {
        return new Injector(ConfigParser.withLocalFile(filePath));
    }

    public static Injector withString(String yaml) throws IOException {
        return new Injector(ConfigParser.withString(yaml));
    }

    public static Injector withInputStream(InputStream input) throws IOException {
        return new Injector(ConfigParser.withInputStream(input));
    }

    private Injector(ConfigParser parser) {
        this.parser = parser;
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
