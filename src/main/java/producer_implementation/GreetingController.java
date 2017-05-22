package producer_implementation;

/**
 * Created by valera on 20.04.2017.
 */

//import com.fasterxml.jackson.core.JsonGenerator;

import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {
    @Autowired
    Sender sender;

    @RequestMapping(value = "/greeting", method = RequestMethod.POST)
    public String createUser(@RequestBody String user){
        sender.send("weather", user);
        return user;
    }
}
