package com.space.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String planet;
  @Enumerated(EnumType.STRING)
  private ShipType shipType;
  private Date prodDate;
  private Boolean isUsed;
  @Column(name = "speed", precision = 2)
  private Double speed;
  private Integer crewSize;
  @Column(name = "rating", precision = 2)
  private Double rating;

  public Ship() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlanet() {
    return planet;
  }

  public void setPlanet(String planet) {
    this.planet = planet;
  }

  public ShipType getShipType() {
    return shipType;
  }

  public void setShipType(ShipType shipType) {
    this.shipType = shipType;
  }

  public Date getProdDate() {
    return prodDate;
  }

  public void setProdDate(Date prodDate) {
    this.prodDate = prodDate;
  }

  public Boolean getUsed() {
    return isUsed;
  }

  public void setUsed(Boolean used) {
    isUsed = used;
  }

  public Double getSpeed() {
    return speed;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public Integer getCrewSize() {
    return crewSize;
  }

  public void setCrewSize(Integer crewSize) {
    this.crewSize = crewSize;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public Boolean isEmpty() {
    return this.getName() == null &&
            this.getPlanet() == null &&
            this.getProdDate() == null &&
            this.getShipType() == null &&
            this.getSpeed() == null &&
            this.getCrewSize() == null;
  }

  public Boolean hasValidName() {
    return this.getName() != null && this.getName().length() <= 50 && !this.getName().isEmpty();
  }

  public Boolean hasValidPlanet() {
    return this.getPlanet() != null && this.getPlanet().length() <= 50 && !this.getPlanet().isEmpty();
  }

  public Boolean hasValidCrewSize() {
    return this.getCrewSize() >= 1 && this.getCrewSize() <= 9999;
  }

  public Boolean hasValidSpeed() {
    return this.getSpeed() != null &&
            !((double) Math.round(this.getSpeed() * 100) / 100 < 0.01) &&
            !((double) Math.round(this.getSpeed() * 100) / 100 > 0.99);
  }

  public Boolean hasValidProdDate() {
    Calendar calendarStart = Calendar.getInstance();
    calendarStart.set(2800, Calendar.JANUARY, 1);

    Calendar calendarEnd = Calendar.getInstance();
    calendarEnd.set(3019, Calendar.DECEMBER, 31);

    return this.prodDate.before(calendarEnd.getTime()) && this.prodDate.after(calendarStart.getTime());
  }

  public Boolean hasValidProperties() {
    return this.hasValidName() &&
            this.hasValidPlanet() &&
            this.hasValidCrewSize() &&
            this.hasValidSpeed() &&
            this.hasValidProdDate();
  }

  public Boolean isValid() {
    if (this.isEmpty())
      return false;

    return this.hasValidProperties();
  }
}