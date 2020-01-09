package config.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

class ConfigParser {

    private Map<String, Object> data;

    static ConfigParser withRemoteFile(String fileUrl) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        URL fileRequest = new URL(fileUrl);
        URLConnection connection = fileRequest.openConnection();
        connection.setDoOutput(true);

        return new ConfigParser(mapper.readValue(fileRequest.openStream(), new TypeReference<HashMap<String, Object>>(){}));
    }

    static ConfigParser withLocalFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return new ConfigParser(mapper.readValue(new File(filePath), new TypeReference<HashMap<String, Object>>(){}));
    }

    static ConfigParser withString(String yaml) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return new ConfigParser(mapper.readValue(yaml, new TypeReference<HashMap<String, Object>>(){}));
    }

    static ConfigParser withInputStream(InputStream input) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return new ConfigParser(mapper.readValue(input, new TypeReference<HashMap<String, Object>>(){}));
    }

    private ConfigParser(Map<String, Object> data) {
        this.data = data;
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
