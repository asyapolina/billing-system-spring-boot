package ru.nexign.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.entity.TariffEntity;

@Repository
public interface TariffsRepository extends JpaRepository<TariffEntity, Integer>  {
    TariffEntity findById(String id);
}
