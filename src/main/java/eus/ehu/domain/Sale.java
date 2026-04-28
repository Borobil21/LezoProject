package eus.ehu.domain;

import jakarta.persistence.Entity;

@Entity
public class Sale extends Listing {

    private Double fixedPrice;

    protected Sale() {
        super();
    }

    private Sale(Builder builder) {
        super(builder.description, builder.status, builder.user);
        this.fixedPrice = builder.fixedPrice;
    }

    public static class Builder {
        private String description;
        private ListingStatus status = ListingStatus.ACTIVE; 
        private User user;
        private Double fixedPrice;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(ListingStatus status) {
            this.status = status;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder fixedPrice(Double fixedPrice) {
            this.fixedPrice = fixedPrice;
            return this;
        }

        public Sale build() {
            return new Sale(this);
        }
    }

    public Double getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(Double fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + getId() +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", price=" + fixedPrice +
                '}';
    }
}