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
    private int id = -1;

    public MainService() {
        Cat c = new Cat();
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
        return new ResponseEntity<Cat>(cats.get(cats.size() - 1), HttpStatus.OK);
    }

    // read
    @RequestMapping(value = "/cats", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Cat>> getCats() {
        ArrayList<Cat> tempArrayList = new ArrayList<Cat>();
        return new ResponseEntity<ArrayList<Cat>>(cats, HttpStatus.OK);
    }

    // get single cat
    @RequestMapping(value = "/cats/{catId}", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Cat>> getSingleCat(@PathVariable int catId) {
        id = catId;
        if (catId < 0 || catId > cats.size() - 1) {
            Map<String, Object> errorMap = new HashMap<String, Object>();
            errorMap.put("ErrorCode", 400);
            errorMap.put("ErrorMessage", "Bad Request");
            return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cats.get(catId) ,HttpStatus.BAD_REQUEST);
    }

    //update
    @RequestMapping(value = "/cats/update", method = RequestMethod.POST)
    public ResponseEntity updateCat(@RequestBody Cat c) {
        id = -1;
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getName().equals(c.getName())) {
                cats.get(i).setName(c.getName());
                cats.get(i).setAge(c.getAge());
                id = i;
                break;
            }
        }
        if (id != -1) {
            return new ResponseEntity(cats.get(id), HttpStatus.OK);
        }
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("ErrorCode", 400);
        errorMap.put("ErrorMessage", "Bad Request");
        return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
    }

    //delete
    @RequestMapping(value = "/cats/delete", method = RequestMethod.POST)
    public ResponseEntity deleteCat(@RequestBody(required = false) Cat c) {
        System.out.println(c.getName());
        id = -1;
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getName().equals(c.getName())) {
                id = i;
                break;
            }
        }

        if (id != -1) {
            Cat deletedCat = cats.remove(id);
            return new ResponseEntity(deletedCat, HttpStatus.OK);
        }
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("ErrorCode", 400);
        errorMap.put("ErrorMessage", "Bad Request");
        return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
    }
}
