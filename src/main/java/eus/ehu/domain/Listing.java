package eus.ehu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public abstract class Listing {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus status;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vehicle vehicle;

    protected Listing() {
    }

    protected Listing(String description, ListingStatus status, User user) {
        this.description = description;
        this.status = status;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }

}
