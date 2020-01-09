package config.parser;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class PackageScanner {

    private final String basePackage;

    PackageScanner(String packageName) {
        this.basePackage = packageName;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @return The classes
     * @throws IOException
     */
    final List<Class<?>> getClasses() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;

        ArrayList<Class<?>> classes = new ArrayList<>();

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());
        for(ClassPath.ClassInfo info : cp.getAllClasses()) {
            try {
                classes.add(info.load());
            } catch (Throwable e) {}
        }

        return classes;
    }
}