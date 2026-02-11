# PickAuto Requirements

## 1. Project Overview
LezoProject is a JavaFX desktop application for buying and selling cars. The app connects buyers and sellers, allowing users to list cars for sale and browse, search, and contact sellers.

## 2. Goals and Scope
- Provide an easy-to-use marketplace for private car listings.
- Support user accounts, listings, and basic communication between buyers and sellers.

## 3. Stakeholders
- Product owner: LezoKapitalFumar.ong
- End users: Buyers and sellers of used cars
- Development team: LezoKapitalFumar.ong

## 4. User Roles
- Guest: Can browse and search listings.
- Registered user: Can create and manage listings, save favorites, and contact sellers.

## 5. Functional Requirements

### 5.1 Authentication and User Management
- FR-1: Users can register with email, password, and basic profile info.
- FR-2: Users can log in and log out.
- FR-3: Users can edit their profile (name, phone, location).
- FR-4: Users can delete their account.

### 5.2 Listings (Cars for Sale)
- FR-5: Registered users can create a car listing with required fields: make, model, year, price, mileage, fuel type, transmission, condition, description, location, and at least one photo.
- FR-6: Users can edit or delete their own listings.
- FR-7: Listings show seller contact info and listing status (active/sold).
- FR-8: Users can mark a listing as sold.

### 5.3 Browsing and Search
- FR-9: Guests and users can browse all active listings.
- FR-10: Users can search by keywords (make, model) and filter by price range, year, mileage, fuel type, transmission...
- FR-11: Users can sort results by price, year, and mileage.

### 5.4 Favorites and Contact
- FR-12: Registered users can save listings as favorites.
- FR-13: Users can contact sellers ¿via an in-app message? or by viewing contact details.

## 6. Non-Functional Requirements
- NFR-1: Passwords must be stored securely.
- NFR-2: Data must be persisted locally (e.g., SQLite) and survive application restarts.
- NFR-3: The UI must be usable with mouse and keyboard only.
- NFR-4: The app must be packaged as a runnable JAR for Windows.

## 7. Data Requirements
- DR-1: Store users, listings, photos (as file paths), favorites, and messages.
- DR-2: Enforce unique user email addresses.

## 8. UI Requirements
- UI-1: Main view shows a list/grid of listings with thumbnail, price, and key info.
- UI-2: Listing details view shows full info, photo gallery, and seller contact.
- UI-3: Provide a create/edit listing form with validation and clear error messages.
- UI-4: Provide search and filter controls on the main view.

## 9. Constraints and Assumptions
- C-1: Java 17 and JavaFX are used.
- C-2: Team has 3 months (12 weeks) to complete the MVP.
- C-3: Single-user desktop app; no multi-user concurrency beyond local data store.
