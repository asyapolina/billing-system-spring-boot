package ru.nexign.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.entity.ClientEntity;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<ClientEntity, Integer> {
    List<ClientEntity> findAll();
}
