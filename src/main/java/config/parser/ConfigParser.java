package config.parser;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ConfigParser {

    private static final String environment = System.getenv("environment");
    private static final String bucket = "www.dashflight.net-config";

    private static Map<String, String> properties;


    ConfigParser(String applicationName) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_2)
                .build();

        String key = String.format("%s/%s.properties", applicationName, environment);

        S3Object configFile;
        try {
            configFile = s3Client.getObject(new GetObjectRequest(bucket, key));
        } catch (AmazonS3Exception ex) {
            key = String.format("%s/%s.properties", applicationName, "production");
            configFile = s3Client.getObject(new GetObjectRequest(bucket, key));
        }

        try {
            properties = new JavaPropsMapper().readValue(configFile.getObjectContent(), new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            properties = new HashMap<>();
            e.printStackTrace();
        }
    }

    String getString(String key) {
        return properties.get(key);
    }

    Integer getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    Double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    Float getFloat(String key) {
        return Float.parseFloat(getString(key));
    }

    Short getShort(String key) {
        return Short.parseShort(getString(key));
    }

    Boolean getBool(String key) {
        return Boolean.parseBoolean(getString(key));
    }

}
