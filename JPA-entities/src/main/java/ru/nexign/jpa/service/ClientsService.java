package ru.nexign.jpa.service;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.jpa.dao.ClientsRepository;
import ru.nexign.jpa.dao.TariffsRepository;
import ru.nexign.jpa.model.ClientEntity;
;import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class ClientsService {
    private final ClientsRepository clientRepo;
    private final TariffsRepository tariffRepo;

    @Autowired
    public ClientsService(ClientsRepository clientRepo, TariffsRepository tariffRepo) {
        this.clientRepo = clientRepo;
        this.tariffRepo = tariffRepo;
    }

    public void addClients()  {
        int numberOfClients = 1000; // Количество генерируемых клиентов

        Set<String> phoneNumbers = new HashSet<>(); // Множество для хранения уже сгенерированных номеров

        // Генерация данных для таблицы clients
        for (int i = 1; i <= numberOfClients; i++) {
            String phoneNumber;
            do {
                phoneNumber = "97" + generateRandomNumber(0, 2) + generateRandomNumber(0, 2) +
                        generateRandomNumber(3, 6) + generateRandomNumber(5, 7)
                        + generateRandomNumber(3, 6)  + generateRandomNumber(5, 7)
                        + generateRandomNumber(7, 9) + generateRandomNumber(7, 9) + generateRandomNumber(7, 9);
            } while (phoneNumbers.contains(phoneNumber)); // Проверка на уникальность номера телефона
            phoneNumbers.add(phoneNumber); // Добавление сгенерированного номера в множество

            BigDecimal balance = BigDecimal.valueOf(generateRandomDouble(-50, 200));
            String tariffId = generateRandomTariffId();

            var tariff = tariffRepo.findById(tariffId);

            clientRepo.save(new ClientEntity(phoneNumber, balance, tariff, new ArrayList<>()));
        }
    }

    // Метод для генерации случайного целого числа в заданном диапазоне
    public static int generateRandomNumber(int min, int max) {
        return new Faker().number().numberBetween(min, max);
    }

    // Метод для генерации случайного числа с плавающей точкой в заданном диапазоне
    public static double generateRandomDouble(int min, int max) {
        return new Faker().number().randomDouble(2, min, max);
    }

    // Метод для генерации случайного значения tariff_id (3, 6 или 11)
    public static String generateRandomTariffId() {
        String[] tariffIds = {"03", "06", "11"};
        return tariffIds[new Faker().number().numberBetween(0, tariffIds.length)];
    }

}
