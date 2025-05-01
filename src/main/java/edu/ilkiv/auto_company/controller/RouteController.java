package edu.ilkiv.auto_company.controller;

import edu.ilkiv.auto_company.dto.RouteDTO;
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
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET route by route number
    @GetMapping("/{routeNumber}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable String routeNumber) {
        return routeService.getRouteById(routeNumber)
                .map(route -> new ResponseEntity<>(route, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST new route
    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@RequestBody RouteDTO routeDTO) {
        if (routeService.existsById(routeDTO.getRouteNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        RouteDTO savedRoute = routeService.saveRoute(routeDTO);
        return new ResponseEntity<>(savedRoute, HttpStatus.CREATED);
    }

    // PUT update route
    @PutMapping("/{routeNumber}")
    public ResponseEntity<RouteDTO> updateRoute(@PathVariable String routeNumber, @RequestBody RouteDTO routeDTO) {
        if (!routeService.existsById(routeNumber)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        routeDTO.setRouteNumber(routeNumber); // Ensure the ID matches
        RouteDTO updatedRoute = routeService.updateRoute(routeNumber, routeDTO);
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
    public ResponseEntity<List<RouteDTO>> getRoutesByName(@RequestParam String keyword) {
        List<RouteDTO> routes = routeService.findByRouteNameContaining(keyword);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET routes by length greater than
    @GetMapping("/length")
    public ResponseEntity<List<RouteDTO>> getRoutesByLengthGreaterThan(@RequestParam Double length) {
        List<RouteDTO> routes = routeService.findByRouteLengthGreaterThan(length);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    // GET routes by average time less than
    @GetMapping("/time")
    public ResponseEntity<List<RouteDTO>> getRoutesByTimeLessThan(@RequestParam Integer time) {
        List<RouteDTO> routes = routeService.findByAverageTimeLessThan(time);
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }
}