package eu.luminis.workshop.smallsteps.logic;

import java.util.UUID;

public interface UserCreatePort {
//    UUID insertUser(CoreUser coreUser);

//    UUID insertUser(String email, String password);

    UUID insertUser(Email email, Password password);
}
