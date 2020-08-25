package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipServiceImplementation;
import com.space.service.ShipSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipRestController {

  @Autowired
  private ShipServiceImplementation shipService;

  @RequestMapping(value = "/ships/{id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<Ship> getShip(@PathVariable("id") Long id) {
    if (id <= 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Ship ship = this.shipService.getById(id);

    if (ship == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(ship, HttpStatus.OK);
  }

  @RequestMapping(value = "/ships", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<Ship> addShip(@RequestBody Ship ship) {
    if (ship.getUsed() == null) {
      ship.setUsed(false);
    }
    if (!ship.isValid()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    ship.setRating(shipService.countRating(ship));
    this.shipService.save(ship);
    return new ResponseEntity<>(ship, HttpStatus.OK);
  }

  @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<Ship> updateShip(@PathVariable("id") Long id, @RequestBody Ship ship) {
    /* Invalid ID:
     * not numeric
     * not integer
     * not positive
     */
    if (id <= 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Ship foundShip = this.shipService.getById(id);

    // Nothing found
    if (foundShip == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Do not update ship with empty request
    if (ship.isEmpty()) {
      return new ResponseEntity<>(foundShip, HttpStatus.OK);
    }

    if (ship.getName() != null) {
      if (ship.hasValidName()) {
        foundShip.setName(ship.getName());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    if (ship.getPlanet() != null) {
      if (ship.hasValidPlanet()) {
        foundShip.setPlanet(ship.getPlanet());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    if (ship.getCrewSize() != null) {
      if (ship.hasValidCrewSize()) {
        foundShip.setCrewSize(ship.getCrewSize());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    if (ship.getProdDate() != null) {
      if (ship.hasValidProdDate()) {
        foundShip.setProdDate(ship.getProdDate());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    if (ship.getSpeed() != null) {
      if (ship.hasValidSpeed()) {
        foundShip.setSpeed(ship.getSpeed());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    if (ship.getShipType() != null) {
      foundShip.setShipType(ship.getShipType());
    }

    if (ship.getUsed() != null) {
      foundShip.setUsed(ship.getUsed());
    }

    foundShip.setRating(shipService.countRating(foundShip));
    this.shipService.save(foundShip);

    return new ResponseEntity<>(foundShip, HttpStatus.OK);
  }

  @RequestMapping(value = "/ships/{id}", method = RequestMethod.DELETE, produces = "application/json")
  public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {
    if (id <= 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Ship ship = this.shipService.getById(id);
    if (ship == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    this.shipService.delete(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/ships", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<Ship>> getAllShips(ShipServiceImplementation.ShipFilters shipFilters,
                                                @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize,
                                                @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
    Page<Ship> ships = this.shipService.findAll(new ShipSpecification(shipFilters), pageable);
    return new ResponseEntity<>(ships.getContent(), HttpStatus.OK);
  }

  @RequestMapping(value = "/ships/count", method = RequestMethod.GET, produces = "application/json")
  public int getShipsCount(ShipServiceImplementation.ShipFilters shipFilters) {
    List<Ship> ships = this.shipService.findAll(new ShipSpecification(shipFilters));
    return ships.size();
  }
}