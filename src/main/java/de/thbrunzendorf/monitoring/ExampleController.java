package de.thbrunzendorf.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ExampleController {

    private Logger logger = LoggerFactory.getLogger(ExampleController.class);

    private ExampleRepository repository;

    public ExampleController(@Autowired ExampleRepository repository) {
        this.repository = repository;
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
    }

    @RequestMapping(method = RequestMethod.GET, path = "/example/{name}")
    public ExampleData getExampleDataByName(@PathVariable String name) {
        logger.info("received example GET by name request " + name);
        return repository.findByName(name);
    }

}
