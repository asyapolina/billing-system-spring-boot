package ru.nexign.brt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nexign.jpa.model.CallEntity;

public interface CallRepository extends JpaRepository<CallEntity, Integer> {
    @Query(value = "SELECT * FROM client_calls ORDER BY end_time DESC LIMIT 1", nativeQuery = true)
    CallEntity findCallEntityWithMaxEndCallTime();
}
