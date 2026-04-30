# LezoProject


## Group name:  lezoKapitalFumar.ong

## Members:

  1. Oier Redondo Lasa (Borobil21)
  2. Pablo Hernandez Lozano (caraco14)
  3. Raul García Orduña (raulGar2)
  4. Jon Rabadán de la Vega (Jonduuu)

## Our project

In LezoKapitalFumar.ong, we dream. That's the reason we have created this project: PickAuto. 
We intend to make an application for buying and selling cars, implementing auctions too, where users can bid for the car they want to buy.

## Shared database setup

The application can connect to a shared H2 server instead of a local-only database. Set one of these before starting the app:

- JVM property: `-Dlezo.db.url=jdbc:h2:tcp://10.245.3.251:9094/./data/lezo`
- Environment variable: `LEZO_DB_URL=jdbc:h2:tcp://10.245.3.251:9094/./data/lezo`

If no URL is provided, the app falls back to a local H2 server on `localhost` for development.

### User experience

The users will be able to:

  - Log In as a Buyer/Seller
  - The buyers will see their options, sorting options and saving their favorites on a personal Wishlist, and buying/bidding (Initial proposes)
  - The sellers will have their view, add new products...

## Forks
- Pablo: https://github.com/caraco14/LezoProject.git
- Raul: https://github.com/raulGar2/LezoProject.git
- Jon: https://github.com/Jonduuu/LezoProject.git
