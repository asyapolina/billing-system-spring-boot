package ru.nexign.brt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.entity.TariffEntity;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Integer> {
    Optional<TariffEntity> findById(String id);
}
