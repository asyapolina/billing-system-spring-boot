package ru.nexign.cdr.generator.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nexign.cdr.generator.PhoneNumberGenerator;
import ru.nexign.jpa.dao.TariffsRepository;
import ru.nexign.jpa.entity.ClientEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ClientGenerator {
    @Value("${const.clients.amount}")
    private int amount;
    private final TariffsRepository tariffRepo;
    private final BalanceGenerator balanceGenerator;

    @Autowired
    public ClientGenerator(TariffsRepository tariffRepo, BalanceGenerator balanceGenerator) {
        this.tariffRepo = tariffRepo;
        this.balanceGenerator = balanceGenerator;
    }

    public List<ClientEntity> createClients() {
        Set<String> phoneNumbers = new HashSet<>(); // Множество для хранения уже сгенерированных номеров
        List<ClientEntity> clients = new ArrayList<>();

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

            clients.add(new ClientEntity(phoneNumber, balance, tariff, new ArrayList<>()));
        }

        return clients;
    }
}
