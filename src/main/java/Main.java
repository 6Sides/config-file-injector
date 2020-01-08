import dashflight.ConfigValue;
import dashflight.Injector;
import java.io.IOException;

public class Main {

    @ConfigValue("test.key")
    private static String test;

    public static void main(String[] args) throws IOException {
        new Injector("test.yaml").inject("");
        System.out.println(test);
    }
}
