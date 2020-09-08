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
    Map<String, Object> errorMap = new HashMap<String, Object>();
    private ArrayList<Car> cars = new ArrayList<Car>();
    private boolean isFound = false;
    private int index = -1;

    public MainService() {
        Car c = new Car();
        Car.totalCar++;

        c.setColor("Gray");
        c.setType("SUV");
        c.setDisplacement(2000);
        c.setSunroof(true);
        c.setSpeed(80);
        c.setId(Car.totalCar);

        cars.add(c);

        errorMap.put("ErrorCode", 400);
        errorMap.put("ErrorMessage", "Bad Request");
    }

    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() {
        return "Hello World";
    }

    //create
    @RequestMapping(value = "/cars", method = RequestMethod.POST)
    public ResponseEntity<Car> createCat(@RequestBody Car c) {
        Car.totalCar++;
        c.setId(Car.totalCar);
        cars.add(c);
        return new ResponseEntity<Car>(c, HttpStatus.OK);
    }

    // read
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Car>> getCats() {
        return new ResponseEntity<ArrayList<Car>>(cars, HttpStatus.OK);
    }

    // get single cat
    @RequestMapping(value = "/cars/{carId}", method = RequestMethod.GET)
    public ResponseEntity getSingleCat(@PathVariable int carId) {
        isFound = isHaveId(carId);

        if (!isFound) {
            return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cars.get(index) ,HttpStatus.BAD_REQUEST);
    }

    //update
    @RequestMapping(value = "/cars/{carId}", method = RequestMethod.PATCH)
    public ResponseEntity updateCat(@PathVariable int carId, @RequestBody Car c) {
        isFound = isHaveId(carId);

        if (!isFound) {
            return new ResponseEntity(errorMap ,HttpStatus.BAD_REQUEST);
        }

        cars.get(index).setColor(c.getColor());
        cars.get(index).setType(c.getType());
        cars.get(index).setDisplacement(c.getDisplacement());
        cars.get(index).setSunroof(c.isSunroof());
        cars.get(index).setSpeed(c.getSpeed());
        return new ResponseEntity(cars.get(index), HttpStatus.OK);
    }

    //delete
    @RequestMapping(value = "/cars/{carId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCat(@PathVariable int carId, @RequestBody(required = false) Cat c) {
        isFound = isHaveId(carId);

        if (!isFound) {
            return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(cars.remove(index), HttpStatus.OK);
    }

    private boolean isHaveId(int id) {
        isFound = false;
        index = -1;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId() == id) {
                isFound = true;
                index = i;
                break;
            }
        }
        return isFound;
    }
}
