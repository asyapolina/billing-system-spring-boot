package ru.nexign.brt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nexign.jpa.entity.CallEntity;

import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<CallEntity, Integer> {
    @Query(value = "SELECT * FROM client_calls ORDER BY end_time DESC LIMIT 1", nativeQuery = true)
    Optional<CallEntity> findCallEntityWithMaxEndCallTime();
}
