package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.AppConfigEntity;
import net.engineeringdigest.journalApp.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    @Autowired
    private AppConfigRepository repository;

    public Map<String, String> cache = new HashMap<>();

    @PostConstruct
    public void init() {
        List<AppConfigEntity> all = repository.findAll();
        for (AppConfigEntity appConfigEntity : all) {
            cache.put(appConfigEntity.getKey(), appConfigEntity.getValue());
        }
    }
}
