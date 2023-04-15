package ru.nexign.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.model.TariffEntity;

@Repository
public interface TariffsRepository extends JpaRepository<TariffEntity, Integer>  {
    TariffEntity findById(String id);
}
