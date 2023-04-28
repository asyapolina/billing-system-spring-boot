package ru.nexign.crm.security.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.dao.ClientsRepository;
import ru.nexign.jpa.dao.UserRepository;
import ru.nexign.jpa.dto.Mapper;
import ru.nexign.jpa.user.UserDto;
import ru.nexign.jpa.user.UserEntity;
import ru.nexign.jpa.user.UserRole;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClientsRepository clientsRepository;
    private final Mapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, ClientsRepository clientsRepository, Mapper mapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.clientsRepository = clientsRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init() {
        var manager = findByUsername("manager");
        if (manager == null) {
            userRepository.save(new UserEntity("manager",
                    "0",
                    encoder.encode("12345"),
                    UserRole.ROLE_MANAGER.toString()));
        }

        var abonent = findByUsername("abonent");
        if (abonent == null) {
            var phoneNumber = clientsRepository.findAll().get(0).getPhoneNumber();
            userRepository.save(new UserEntity("abonent",
                    phoneNumber,
                    encoder.encode("abcde"),
                    UserRole.ROLE_ABONENT.toString()));
        }
    }

    public void register(UserDto user) {
        var entity = mapper.toEntity(user);
        entity.setPassword(encoder.encode(user.getPassword()));
        entity.setRole(UserRole.ROLE_ABONENT.toString());
        userRepository.save(entity);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
