Description
The users are allowed to search for cars on the web site using different filtering criteria such as brand,model,price,mileage,year.

Preconditions
  The system must have available cars.
Postconditions
  The results match with the search criteria

Main Event Flow

1. The user accesses the car search page
2. The system displays the search with the following filters:
  2.1 Car brand
  2.2 Model
  2.3 Price
  2.4 Mileage
  2.5 Year of manufacture
  2.6 Fuel type
3. The user enters the search criteria
4. The users clicks the "Search" button
5. The system validates the search information
6. The system retrieves cars that match the criteria.
7. The system displays the list of matching cars.

Alternative Event Flow

At step 6, if no cars match with the criteria:
  1. The system shows a message saying there's no cars with that criteria
  2. The user modify the search filters
  3. The process continues from step 3 of the Main Flow
