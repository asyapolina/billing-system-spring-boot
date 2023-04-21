package ru.nexign.jpa.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto implements Serializable {
    private String username;
    private String password;

}
