package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by toff63 on 13/12/2014.
 */

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();
    private final static String FORMAT = "Hello, %s!";

    @RequestMapping("/greeting")
    public Greeting greet(@RequestParam(value = "name", defaultValue = "World") String name){
        return new Greeting(counter.incrementAndGet(),
                            String.format(FORMAT, name));
    }
}
