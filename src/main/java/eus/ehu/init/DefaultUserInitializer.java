package eus.ehu.init;

import eus.ehu.businesslogic.BlManager;
import eus.ehu.domain.User;

public class DefaultUserInitializer {

    public static void main(String[] args) {
        System.out.println("=== Initializing Default User ===\n");
        BlManager bl = null;

        try {
            bl = new BlManager();
            User defaultUser = bl.getUserByEmail("default@gmail.com");

            if (defaultUser == null) {
                defaultUser = new User("Default User", "default@gmail.com", "1234", "999999999", "Lezo");
                System.out.println("Storing default user: " + defaultUser.getName());
                bl.storeUser(defaultUser);
            } else {
                System.out.println("Default user already exists: " + defaultUser.getEmail());
            }

            User fetchedUser = bl.getUserByEmail("default@gmail.com");
            if (fetchedUser != null) {
                System.out.println("User verified in database: " + fetchedUser.getEmail());
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (bl != null) {
                bl.close();
            }
        }
    }

}
