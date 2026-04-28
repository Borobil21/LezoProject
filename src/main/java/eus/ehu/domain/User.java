package eus.ehu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private String location;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Listing> publications;

	protected User() {
		this.publications = new ArrayList<>();
	}

	private User(Builder builder) {
		this.name = builder.name;
		this.email = builder.email;
		this.password = builder.password;
		this.phone = builder.phone;
		this.location = builder.location;
		this.publications = new ArrayList<>();
	}

	public static class Builder {
		private String name;
		private String email;
		private String password;
		private String phone;
		private String location;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public Builder location(String location) {
			this.location = location;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Listing> getPublications() {
		return publications;
	}

	public void setPublications(List<Listing> publications) {
		this.publications = publications;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", phone='" + phone + '\'' +
				", location='" + location + '\'' +
				", publicationsCount=" + (publications != null ? publications.size() : 0) +
				'}';
	}

	// ManyToMany relationship for the wishlist, a user can have many vehicles in their wishlist and a vehicle can be in the wishlist of many users
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_wishlist", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "vehicle_id")
	)

	private List<Vehicle> wishlist = new ArrayList<>();

	// Getters and Setters
	public List<Vehicle> getWishlist() { 
		return wishlist; 
	}
	public void addToWishlist(Vehicle v) { 
		this.wishlist.add(v);
	}
}
