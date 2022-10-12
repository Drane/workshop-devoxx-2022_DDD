package eu.luminis.workshop.smallsteps.logic;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterUserCommand {
    String email;
    String password;
}
