package ru.nexign.brt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.TariffRepository;
import ru.nexign.brt.exception.TariffNotFoundException;
import ru.nexign.jpa.entity.TariffEntity;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Cacheable("tariff")
    public TariffEntity getTariff(String id) {
        return tariffRepository.findById(id).orElseThrow(
                () -> new TariffNotFoundException("Tariff with id " + id + " doesn't exist."));
    }
}
