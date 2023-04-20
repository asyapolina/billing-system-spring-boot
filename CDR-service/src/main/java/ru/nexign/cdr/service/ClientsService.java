package ru.nexign.cdr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nexign.cdr.generator.client.BalanceGenerator;
import ru.nexign.cdr.generator.PhoneNumberGenerator;
import ru.nexign.cdr.generator.client.TariffGenerator;
import ru.nexign.jpa.dao.ClientsRepository;
import ru.nexign.jpa.dao.TariffsRepository;
import ru.nexign.jpa.entity.ClientEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClientsService {
    @Value("${const.clients.amount}")
    private int amount;
    private final ClientsRepository clientRepo;
    private final TariffsRepository tariffRepo;

    private final BalanceGenerator balanceGenerator;

    @Autowired
    public ClientsService(ClientsRepository clientRepo, TariffsRepository tariffRepo, BalanceGenerator balanceGenerator) {
        this.clientRepo = clientRepo;
        this.tariffRepo = tariffRepo;
        this.balanceGenerator = balanceGenerator;
    }

    public void generateClients()  {
        Set<String> phoneNumbers = new HashSet<>(); // Множество для хранения уже сгенерированных номеров

        // Генерация данных для таблицы clients
        for (int i = 1; i <= amount; i++) {
            String phoneNumber;
            do {
                phoneNumber = PhoneNumberGenerator.generateRussianPhoneNumber();
            } while (phoneNumbers.contains(phoneNumber)); // Проверка на уникальность номера телефона
            phoneNumbers.add(phoneNumber); // Добавление сгенерированного номера в множество

            BigDecimal balance = BigDecimal.valueOf(balanceGenerator.generateBalance());
            String tariffId = TariffGenerator.generateRandomTariffId();

            var tariff = tariffRepo.findById(tariffId);

            clientRepo.save(new ClientEntity(phoneNumber, balance, tariff, new ArrayList<>()));
        }
    }
}
