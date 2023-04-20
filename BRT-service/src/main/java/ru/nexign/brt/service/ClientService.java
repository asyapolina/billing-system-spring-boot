package ru.nexign.brt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ru.nexign.jpa.entity.ReportEntity;
import ru.nexign.jpa.request.body.DepositRequestBody;
import ru.nexign.jpa.request.body.TariffRequestBody;
import ru.nexign.jpa.response.body.DepositResponseBody;
import ru.nexign.jpa.response.body.ReportResponseBody;
import ru.nexign.jpa.response.body.TariffResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ClientService {
    @Value("${const.monetary.unit}")
    private String monetaryUnit;
    @Value("${const.phone.number.length}")
    private int phoneNumberLength;
    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;
    private final CallRepository callRepository;
    private final ReportRepository reportRepository;
    private final Mapper mapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, TariffRepository tariffRepository, CallRepository callRepository, ReportRepository reportRepository, Mapper mapper) {
        this.clientRepository = clientRepository;
        this.tariffRepository = tariffRepository;
        this.callRepository = callRepository;
        this.reportRepository = reportRepository;
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

    public ClientDto getByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != phoneNumberLength) {
            throw new BrtException("Incorrect phone number.");
        }
        var client = clientRepository.findByPhoneNumber(phoneNumber);
        return client.map(mapper::toDto).orElse(null);
    }

    public TariffResponseBody changeTariff(TariffRequestBody request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + request.getPhoneNumber() + " doesn't exist."));
        var tariff = tariffRepository.findById(request.getTariffId()).orElseThrow(
                () -> new TariffNotFoundException("Tariff with id " + request.getTariffId() + " doesn't exist."));

        client.setTariff(tariff);
        clientRepository.save(client);
        return new TariffResponseBody(client.getId(), client.getPhoneNumber(), tariff.getId());
    }

    public DepositResponseBody depositMoney(DepositRequestBody request) {
        var client = clientRepository.findByPhoneNumber(request.getPhoneNumber()).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + request.getPhoneNumber() + " doesn't exist."));

        client.setBalance(client.getBalance().add(request.getMoney()));
        clientRepository.save(client);
        return new DepositResponseBody(client.getId(), client.getPhoneNumber(), client.getBalance());
    }

    public ClientDto withdrawMoney(String phoneNumber, BigDecimal money) {
        var client = clientRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + phoneNumber + " doesn't exist."));

        if (money != null) {
            client.setBalance(client.getBalance().subtract(money));
        }
        clientRepository.save(client);
        return mapper.toDto(client);
    }

    public void AddReport(String phoneNumber, List<CallDto> calls, BigDecimal price) {
        if (phoneNumber == null || phoneNumber.length() != phoneNumberLength) {
            throw new BrtException("Incorrect phone number.");
        }
        var client = clientRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + phoneNumber + " doesn't exist."));

        var report = new ReportEntity(price, monetaryUnit, client);
        report.setCalls(mapper.toEntity(calls, report));
        reportRepository.save(report);
        clientRepository.save(client);
    }

    public ReportResponseBody getLastReport(String phoneNumber) {
        var client  = clientRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ClientNotFoundException("Client with phone number " + phoneNumber + " doesn't exist."));

        List<ReportEntity> reports = client.getReports();
        ReportEntity report;
        if (!reports.isEmpty()) {
            report = reports.get(reports.size() - 1);

            return new ReportResponseBody(report.getId(),
                    phoneNumber,
                    client.getTariff().getId(),
                    report.getCalls(),
                    report.getTotalCost(),
                    report.getMonetaryUnit());

        } else {
            throw new BrtException("Client doesn't have any reports.");
        }

    }
}
