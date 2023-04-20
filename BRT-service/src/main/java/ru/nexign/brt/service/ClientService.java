package ru.nexign.brt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.CallRepository;
import ru.nexign.brt.dao.ClientRepository;
import ru.nexign.brt.dao.ReportRepository;
import ru.nexign.brt.dao.TariffRepository;
import ru.nexign.brt.exception.BrtException;
import ru.nexign.brt.exception.ClientNotFoundException;
import ru.nexign.brt.exception.TariffNotFoundException;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.dto.Mapper;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.response.body.DepositResponseBody;
import ru.nexign.jpa.response.body.TariffResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ClientService {
    @Value("${const.phone.number.length}")
    private int phoneNumberLength;
    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;
    private final Mapper mapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, TariffRepository tariffRepository, Mapper mapper) {
        this.clientRepository = clientRepository;
        this.tariffRepository = tariffRepository;
        this.mapper = mapper;
    }

    public ClientDto createClient(ClientDto clientDto) {
        if (clientRepository.findByPhoneNumber(clientDto.getPhoneNumber()).isPresent()) {
            throw new BrtException("Client with phone number " + clientDto.getPhoneNumber() + " already exists.");
        }

        var tariff = tariffRepository.findById(clientDto.getTariffId()).orElseThrow(
                () -> new TariffNotFoundException("Tariff with id " + clientDto.getTariffId() + " doesn't exist."));
        clientRepository.save(mapper.toEntity(clientDto, tariff));
        return clientDto;
    }

    @Cacheable("clients")
    public ClientDto getByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != phoneNumberLength) {
            throw new BrtException("Incorrect phone number.");
        }
        var client = clientRepository.findByPhoneNumber(phoneNumber);
        return client.map(mapper::toDto).orElse(null);
    }

    @CachePut(value = "clients", key = "#request.phoneNumber")
    public TariffResponseBody changeTariff(TariffRequestBody request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + request.getPhoneNumber() + " doesn't exist."));
        var tariff = tariffRepository.findById(request.getTariffId()).orElseThrow(
                () -> new TariffNotFoundException("Tariff with id " + request.getTariffId() + " doesn't exist."));

        client.setTariff(tariff);
        clientRepository.save(client);
        return new TariffResponseBody(client.getId(), client.getPhoneNumber(), tariff.getId());
    }

    @CachePut(value = "clients", key = "#request.phoneNumber")
    public DepositResponseBody depositMoney(DepositRequestBody request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + request.getPhoneNumber() + " doesn't exist."));

        client.setBalance(client.getBalance().add(request.getMoney()));
        clientRepository.save(client);
        return new DepositResponseBody(client.getId(), client.getPhoneNumber(), client.getBalance());
    }

    @CachePut(value = "clients", key = "#phoneNumber")
    public ClientDto withdrawMoney(String phoneNumber, BigDecimal money) {
        var client = clientRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + phoneNumber + " doesn't exist."));

        if (money != null) {
            client.setBalance(client.getBalance().subtract(money));
        }
        clientRepository.save(client);
        return mapper.toDto(client);
    }
}
