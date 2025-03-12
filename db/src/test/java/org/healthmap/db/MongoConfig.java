package org.healthmap.db;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.healthmap.db.mongodb.repository")

public class MongoConfig {
    @Value("${data.mongodb.user}")
    private String user;
    @Value("${data.mongodb.password}")
    private String password;
    @Value("${data.mongodb.authSource}")
    private String authSource;

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://" + user + ":" + password + "@localhost:27018/healthmap?authSource=" + authSource), "healthmap");
    }

}
