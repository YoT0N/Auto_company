package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.model.Route;
import edu.ilkiv.auto_company.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // GET all routes
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.getAllRoutes();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET route by route number
    @GetMapping("/{routeNumber}")
    public ResponseEntity<Route> getRouteById(@PathVariable String routeNumber) {
        return routeService.getRouteById(routeNumber)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new route
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        if (routeService.existsById(route.getRouteNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Route savedRoute = routeService.saveRoute(route);
        return new ResponseEntity<>(savedRoute, HttpStatus.CREATED);
    }

    // PUT update route
    @PutMapping("/{routeNumber}")
    public ResponseEntity<Route> updateRoute(@PathVariable String routeNumber, @RequestBody Route route) {
        if (!routeService.existsById(routeNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        route.setRouteNumber(routeNumber); // Ensure the ID matches
        Route updatedRoute = routeService.updateRoute(routeNumber, route);
        return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
    }

    // DELETE route
    @DeleteMapping("/{routeNumber}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String routeNumber) {
        if (!routeService.existsById(routeNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        routeService.deleteRoute(routeNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET routes by name containing keyword
    @GetMapping("/search")
    public ResponseEntity<List<Route>> getRoutesByName(@RequestParam String keyword) {
        List<Route> routes = routeService.findByRouteNameContaining(keyword);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET routes by length greater than
    @GetMapping("/length")
    public ResponseEntity<List<Route>> getRoutesByLengthGreaterThan(@RequestParam Double length) {
        List<Route> routes = routeService.findByRouteLengthGreaterThan(length);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET routes by average time less than
    @GetMapping("/time")
    public ResponseEntity<List<Route>> getRoutesByTimeLessThan(@RequestParam Integer time) {
        List<Route> routes = routeService.findByAverageTimeLessThan(time);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }
}