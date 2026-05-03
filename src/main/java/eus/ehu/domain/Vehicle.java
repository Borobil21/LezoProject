package eus.ehu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Vehicle {

	@Id
	@Column(name = "license_plate", updatable = false, nullable = false, length = 20)
	private String licensePlate;

	private String make;
	private String model;
	@Column(name = "vehicle_year")
	private int year;

	private Integer mileage;
	private Integer horsepower;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MotorType motorType;

	@OneToOne
	@JoinColumn(name = "listing_id", nullable = false, unique = true)
	private Listing listing;

	@Column(name = "image_path")
	private String imagePath;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	protected Vehicle() {
	}

	private Vehicle(Builder builder) {
		this.licensePlate = builder.licensePlate;
		this.make = builder.make;
		this.model = builder.model;
		this.year = builder.year;
		this.mileage = builder.mileage;
		this.horsepower = builder.horsepower;
		this.motorType = builder.motorType;
		this.listing = builder.listing;
		this.imagePath = builder.imagePath;	
	}

	public static class Builder {
		private String licensePlate;
		private String make;
		private String model;
		private int year;
		private Integer mileage;
		private Integer horsepower;
		private MotorType motorType;
		private Listing listing;
		private String imagePath;

		public Builder(String licensePlate) {
			this.licensePlate = licensePlate;
		}

		public Builder make(String make) {
			this.make = make;
			return this;
		}

		public Builder model(String model) {
			this.model = model;
			return this;
		}

		public Builder year(int year) {
			this.year = year;
			return this;
		}

		public Builder mileage(Integer mileage) {
			this.mileage = mileage;
			return this;
		}

		public Builder horsepower(Integer horsepower) {
			this.horsepower = horsepower;
			return this;
		}

		public Builder motorType(MotorType motorType) {
			this.motorType = motorType;
			return this;
		}

		public Builder listing(Listing listing) {
			this.listing = listing;
			return this;
		}

		public Vehicle build() {
			return new Vehicle(this);
		}
		public Builder imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    	}
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

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public Integer getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(Integer horsepower) {
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
		String displayMileage = (mileage != null) ? mileage + " km" : "No data";
		String displayHorsepower = (horsepower != null) ? horsepower + " CV" : "No data";

		return "Vehicle{" +
				"licensePlate='" + licensePlate + '\'' +
				", make='" + make + '\'' +
				", model='" + model + '\'' +
				", year=" + year +
				", mileage=" + displayMileage +
				", horsepower=" + displayHorsepower +
				", motorType=" + motorType +
				'}';
	}
}