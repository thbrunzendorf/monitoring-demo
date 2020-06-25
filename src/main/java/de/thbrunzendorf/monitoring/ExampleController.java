package de.thbrunzendorf.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ExampleController {

    private Logger logger = LoggerFactory.getLogger(ExampleController.class);

    private ExampleRepository repository;

    private MeterRegistry meterRegistry;

    public ExampleController(@Autowired ExampleRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/example")
    public Collection<ExampleData> getExampleDataAll() {
        logger.info("received example GET all request");
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/example")
    public void createExampleData(@RequestBody ExampleData data) {
        logger.info("received example POST request");
        repository.create(data);
        meterRegistry.counter("exampleData.created", "name", data.getName()).increment(data.getValue());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/example/{name}")
    public ExampleData getExampleDataByName(@PathVariable String name) {
        logger.info("received example GET by name request " + name);
        return repository.findByName(name);
    }

}
