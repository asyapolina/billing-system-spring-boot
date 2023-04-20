package ru.nexign.brt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nexign.jpa.entity.CallEntity;
import ru.nexign.jpa.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
}
