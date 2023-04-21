package ru.nexign.brt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nexign.brt.dao.ClientRepository;
import ru.nexign.brt.dao.ReportRepository;
import ru.nexign.brt.exception.BrtException;
import ru.nexign.brt.exception.ClientNotFoundException;
import ru.nexign.brt.exception.UserHasNoAccessException;
import ru.nexign.jpa.dao.UserRepository;
import ru.nexign.jpa.dto.CallDto;
import ru.nexign.jpa.dto.Mapper;
import ru.nexign.jpa.entity.ReportEntity;
import ru.nexign.jpa.response.body.ReportResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {
    @Value("${const.phone.number.length}")
    private int phoneNumberLength;
    @Value("${const.monetary.unit}")
    private String monetaryUnit;
    private final ReportRepository reportRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public ReportService(ReportRepository reportRepository, ClientRepository clientRepository, UserRepository userRepository, Mapper mapper) {
        this.reportRepository = reportRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @CachePut(value = "clientReports", key = "#phoneNumber")
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

    @Cacheable("clientReports")
    public ReportResponseBody getLastReport(String phoneNumber, String username) {
        var user = userRepository.findByUsername(username);

        if (user.getPhoneNumber().equals(phoneNumber)) {
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
        } else {
            throw new UserHasNoAccessException("You don't have access to a foreign phone number.");
        }


    }
}
