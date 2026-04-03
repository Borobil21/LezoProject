package eus.ehu.domain;

import javax.persistence.Entity;

@Entity
public class Sale extends Listing {

    private double fixedPrice;

    protected Sale() {
    }

    public Sale(String description, ListingStatus status, User user, double fixedPrice) {
        super(description, status, user);
        this.fixedPrice = fixedPrice;
    }

    public double getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(double fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + getId() +
                ", fixedPrice=" + fixedPrice +
                ", status=" + getStatus() +
                '}';
    }

}
