package ru.nexign.jpa.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import ru.nexign.jpa.entity.ClientEntity;

import javax.persistence.*;

@Table(name="users")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private String role;

    public UserEntity(String username, String phoneNumber, String password, String role) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }
}
