package hello;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private static Map<String,String> defaultTags = new HashMap<>();
    static{
        defaultTags.put("region", "Saratov");
        //defaultTags.put("Hbar", "100");
    }

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    //working with JDBC
    String createTags(Map<String,String> map){
        StringBuilder s = new StringBuilder();
        for(Map.Entry<String, String> m : map.entrySet()){
            s.append(",");
//            s.append(" ");
            s.append(m.getKey());
            s.append("=");
            s.append(m.getValue());
        }
        s.append(" ");
        return new String(s);
    }

    //working with Generator
    String createValues(Map<String,String> map) {
        String s = createTags(map);
        StringBuilder s1 = new StringBuilder(s);
        s1.delete(0, 1);
        s1.deleteCharAt(s1.length() - 1);
        return new String(s1);
    }

    String createDataPoint(String tags, String values, long dateMs){
        return "weather" + tags + values + " " + dateMs;
    }

    String createDataPoint(String tags, String values){
        return "weather" + tags + values;
    }

    String createDataPoint(Map<String, String> tags, Map<String, String> values, long dateMs){
        return "weather" + createTags(tags) + createValues(values) + " " + dateMs;
    }

    String createDataPoint(Map<String, String> tags, Map<String, String> values){
        return "weather" + createTags(tags) + createValues(values);
    }

    @KafkaListener(topics = "weather")
    public void receive(String message) {
        LOGGER.info("received message='{}'", message);
        latch.countDown();
        System.out.println(latch);
        SimpleParser parser = new SimpleParser();
        Map<String,String> map = parser.getData(message);
        //here should be JDBC part
        Map<String,String> values = parser.getWeather(map);
        String s = createDataPoint(createTags(defaultTags), createValues(values));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8086/write?db=openhub_db");
        Invocation invocation = target.request().buildPost(Entity.entity(s, MediaType.TEXT_PLAIN));
        Response response = invocation.invoke();
        String body = response.readEntity(String.class);
        System.out.println(body);
        response.close();
    }
}
