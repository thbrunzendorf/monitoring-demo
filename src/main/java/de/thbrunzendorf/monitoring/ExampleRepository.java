package de.thbrunzendorf.monitoring;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ExampleRepository {
    private Map<String, ExampleData> inMemoryData = new HashMap<>();

    public void create(ExampleData data) {
        inMemoryData.put(data.getName(), data);
    }

    public ExampleData findByName(String name) {
        return inMemoryData.get(name);
    }

    public Collection<ExampleData> findAll() {
        return inMemoryData.values();
    }
}
