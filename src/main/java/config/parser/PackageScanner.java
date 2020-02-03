package config.parser;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    private final String basePackage;

    public PackageScanner(String packageName) {
        this.basePackage = packageName;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @return The classes
     * @throws IOException
     */
    public final List<Class<?>> getClasses() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;


        ArrayList<Class<?>> classes = new ArrayList<>();

        ClassPath cp = ClassPath.from(Thread.currentThread().getContextClassLoader());

        ImmutableSet<ClassInfo> scannedClasses;
        if (this.basePackage == null || this.basePackage.equals("")) {
            scannedClasses = cp.getAllClasses();
        } else {
            scannedClasses = cp.getTopLevelClassesRecursive(this.basePackage);
        }

        for(ClassInfo info : scannedClasses) {
            try {
                classes.add(info.load());
            } catch (Throwable ignored) {}
        }

        return classes;
    }
}
