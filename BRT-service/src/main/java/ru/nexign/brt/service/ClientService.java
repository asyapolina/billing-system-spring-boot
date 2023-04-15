package ru.nexign.brt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.ClientRepository;
import ru.nexign.brt.dao.TariffRepository;
import ru.nexign.jpa.dto.ClientDto;
import ru.nexign.jpa.dto.Mapper;
import ru.nexign.jpa.request.DepositRequest;
import ru.nexign.jpa.request.TariffRequest;
import ru.nexign.jpa.response.DepositResponse;
import ru.nexign.jpa.response.TariffResponse;

import java.math.BigDecimal;

@Service
public class ClientService {
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
        if (clientRepository.findByPhoneNumber(clientDto.getPhoneNumber()) != null) {
            // exception
        }

        var tariff = tariffRepository.findById(clientDto.getTariffId());
        clientRepository.save(mapper.toEntity(clientDto, tariff));
        return clientDto;
    }

    public ClientDto getByPhoneNumber(String phoneNumber) {
        var client = clientRepository.findByPhoneNumber(phoneNumber);
        if (client == null) {
            // exception
        }
        return mapper.toDto(client);
    }

    public TariffResponse changeTariff(TariffRequest request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber());
        var tariff = tariffRepository.findById(request.getTariffId());
        if (client == null) {
            // exception
        }
        if (tariff == null) {
            // exception
        }

        client.setTariff(tariff);
        clientRepository.save(client);
        return new TariffResponse(client.getId(), client.getPhoneNumber(), tariff.getId());
    }

    public DepositResponse depositMoney(DepositRequest request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber());
        if (client == null) {
            // exception
        }

        client.setBalance(client.getBalance().add(request.getMoney()));
        clientRepository.save(client);
        return new DepositResponse(client.getId(), client.getPhoneNumber(), client.getBalance());
    }

    public ClientDto withdrawMoney(String phoneNumber, BigDecimal money) {
        var client = clientRepository.findByPhoneNumber(phoneNumber);
        if (client == null) {
            // exception
        }

        client.setBalance(client.getBalance().subtract(money));
        clientRepository.save(client);
        return mapper.toDto(client);
    }
}