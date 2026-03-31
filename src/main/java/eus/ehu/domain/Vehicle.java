package eus.ehu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Vehicle {

	@Id
	@Column(name = "license_plate", updatable = false, nullable = false, length = 20)
	private String licensePlate;

	private String make;
	private String model;
	@Column(name = "vehicle_year")
	private int year;
	private int mileage;
	private int horsepower;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MotorType motorType;

	@OneToOne
	@JoinColumn(name = "listing_id", nullable = false, unique = true)
	private Listing listing;

	protected Vehicle() {
	}

	public Vehicle(String licensePlate, String make, String model, int year, int mileage, int horsepower,
			MotorType motorType, Listing listing) {
		this.licensePlate = licensePlate;
		this.make = make;
		this.model = model;
		this.year = year;
		this.mileage = mileage;
		this.horsepower = horsepower;
		this.motorType = motorType;
		this.listing = listing;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public int getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(int horsepower) {
		this.horsepower = horsepower;
	}

	public MotorType getMotorType() {
		return motorType;
	}

	public void setMotorType(MotorType motorType) {
		this.motorType = motorType;
	}

	public Listing getListing() {
		return listing;
	}

	public void setListing(Listing listing) {
		this.listing = listing;
	}

	@Override
	public String toString() {
		return "Vehicle{" +
				"licensePlate='" + licensePlate + '\'' +
				", make='" + make + '\'' +
				", model='" + model + '\'' +
				", year=" + year +
				", mileage=" + mileage +
				", horsepower=" + horsepower +
				", motorType=" + motorType +
				", listingId=" + (listing != null ? listing.getId() : null) +
				'}';
	}

}
