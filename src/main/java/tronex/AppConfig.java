package tronex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${tron.backend.host}")
    public String tronHost;
    @Value("${tron.backend.port}")
    public String tronPort;

    @Value("${mongodb.host}")
    public String mongodbHost;
    @Value("${mongodb.port}")
    public String mongodbPort;
}
