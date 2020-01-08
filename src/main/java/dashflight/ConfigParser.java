package dashflight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ConfigParser {

    private Map<String, Object> data;

    ConfigParser(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        data = mapper.readValue(new File(filename), new TypeReference<HashMap<String, Object>>(){});
    }

     Object getObject(String key) {
        String[] parts = key.split("\\.");

        if (parts.length == 1) {
            return data.get(parts[0]);
        }

        Map<String, Object> next = null; 
        for (int i = 0; i < parts.length-1; i++) {
            next = (Map<String, Object>) data.get(parts[i]);
        }

        return next.get(parts[parts.length - 1]);
    }

    String getString(String key) {
        return (String) this.getObject(key);
    }

    Integer getInt(String key) {
        return (Integer) this.getObject(key);
    }

    Boolean getBool(String key) {
        return (Boolean) this.getObject(key);
    }

}
