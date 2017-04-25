package hello;

/**
 * Created by valera on 20.04.2017.
 */
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

//import com.fasterxml.jackson.core.JsonGenerator;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class GreetingController {
//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();
    @Autowired
    Sender sender;
    @Autowired
    Receiver receiver;



//    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
//    public Greeting greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
//    public Greeting greeting(String name) {
//        return new Greeting(counter.incrementAndGet(),
//                String.format(template, name));
//    }
    @RequestMapping(value = "/greeting", method = RequestMethod.POST)
    public String createUser(@RequestBody String user){
        System.out.println(user);
        sender.send("weather", user);
        return user;
    }
}
