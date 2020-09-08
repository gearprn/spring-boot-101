package gearprn.spring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class MainService {
    private ArrayList<Cat> cats = new ArrayList<Cat>();

    public MainService() {
        Cat c = new Cat();
        Cat.id++;
        c.setName("Taro");
        c.setAge(10);
        cats.add(c);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() {
        return "Hello World";
    }

    //create
    @RequestMapping(value = "/cats", method = RequestMethod.POST)
    public ResponseEntity<Cat> createCat(@RequestBody Cat c) {
        cats.add(c);
        Cat.id++;
        return new ResponseEntity<Cat>(cats.get(cats.size() - 1), HttpStatus.OK);
    }

    // read
    @RequestMapping(value = "/cats", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Map<String, Object>>> getCats() {
        ArrayList<Map<String, Object>> response =  new ArrayList<Map<String, Object>>();
        for (int i = 0; i < cats.size(); i++) {
            Map<String, Object> catMap = new HashMap<String, Object>();
            catMap.put("id", i + 1);
            catMap.put("name", cats.get(i).getName());
            catMap.put("age", cats.get(i).getAge());
            response.add(catMap);
        }
        return new ResponseEntity<ArrayList<Map<String, Object>>>(response, HttpStatus.OK);
    }

    // get single cat
    @RequestMapping(value = "/cats/{catId}", method = RequestMethod.GET)
    public ResponseEntity getSingleCat(@PathVariable int catId) {
        if (catId < 1 || catId > cats.size()) {
            Map<String, Object> errorMap = new HashMap<String, Object>();
            errorMap.put("ErrorCode", 400);
            errorMap.put("ErrorMessage", "Bad Request");
            return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> catMap = new HashMap<String, Object>();
        catMap.put("id", catId);
        catMap.put("name", cats.get(catId - 1).getName());
        catMap.put("age", cats.get(catId - 1).getAge());
        return new ResponseEntity(catMap ,HttpStatus.BAD_REQUEST);
    }

    //update
    @RequestMapping(value = "/cats/{catId}", method = RequestMethod.PATCH)
    public ResponseEntity updateCat(@PathVariable int catId, @RequestBody Cat c) {
        if (catId > 0 && catId <= cats.size()) {
            cats.get(catId - 1).setName(c.getName());
            cats.get(catId - 1).setAge(c.getAge());
            return new ResponseEntity(cats.get(catId - 1), HttpStatus.OK);
        }
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("ErrorCode", 400);
        errorMap.put("ErrorMessage", "Bad Request");
        return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
    }

    //delete
    @RequestMapping(value = "/cats/{catId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCat(@PathVariable int catId, @RequestBody(required = false) Cat c) {
        if (catId > 0 && catId <= cats.size()) {
            return new ResponseEntity(cats.remove(catId - 1), HttpStatus.OK);
        }
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("ErrorCode", 400);
        errorMap.put("ErrorMessage", "Bad Request");
        return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
    }
}
