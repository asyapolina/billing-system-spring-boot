package ru.nexign.crm.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nexign.crm.security.repository.UserRepository;
import ru.nexign.jpa.dto.Mapper;
import ru.nexign.jpa.user.UserDto;
import ru.nexign.jpa.user.UserEntity;
import ru.nexign.jpa.user.UserRole;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init() {
        var user = findByUsername("manager");

        if (user.isEmpty()) {
            userRepository.save(new UserEntity("manager",
                    "0",
                    encoder.encode("12345"),
                    UserRole.ROLE_MANAGER.toString()));
        }
    }

    public void register(UserDto user) {
        var entity = mapper.toEntity(user);
        entity.setPassword(encoder.encode(user.getPassword()));
        entity.setRole(UserRole.ROLE_ABONENT.toString());
        userRepository.save(entity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<UserEntity> findByNameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
