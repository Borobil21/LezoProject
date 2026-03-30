package eus.ehu.domain;

public class DomainCreationCheck {

    public static void main(String[] args) {
        User user = new User(
                "Raul Garcia",
                "raul@example.com",
                "secret123",
                "600123123",
                "Bilbao");

        Sale sale = new Sale(
                "BMW 320d bien cuidado",
                "Bilbao",
                ListingStatus.ACTIVE,
                user,
                15499.99);

        Vehicle vehicle = new Vehicle(
                "1234ABC",
                "BMW",
                "320d",
                2018,
                98000,
                190,
                MotorType.DIESEL,
                sale);

        System.out.println("All domain objects were created successfully.");
        System.out.println(user);
        System.out.println(sale);
        System.out.println(vehicle);
    }
}
