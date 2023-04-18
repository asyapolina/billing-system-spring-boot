package ru.nexign.brt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    Optional<ClientEntity> findByPhoneNumber(String phoneNumber);
}
