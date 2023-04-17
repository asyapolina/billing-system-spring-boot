package ru.nexign.hrs.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.nexign.hrs.service.tariffication.TarifficationService;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.response.ClientReport;
import ru.nexign.jpa.response.ReportResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportGeneratorService {
    private final ApplicationContext applicationContext;

    @Autowired
    public ReportGeneratorService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

   public ReportResponse generateReport(TarifficationRequest request) {
       ReportResponse response = new ReportResponse(new ArrayList<>());
       Map<String, ClientReport> responseMap = new HashMap<>();

       for (CallDataRecord cdr : request.getCdrList()) {
           var tarifficationService = applicationContext.getBean(cdr.getTariff().getName(), TarifficationService.class);
           if (tarifficationService == null) {
               // exception
           }

           String phoneNumber = cdr.getPhoneNumber();
           ClientReport report = responseMap.get(phoneNumber);

           if (report == null) {
               report = new ClientReport(phoneNumber, BigDecimal.ZERO, new ArrayList<>());
               responseMap.put(phoneNumber, report);
               response.getClientReports().add(report);
           }

           ClientReport fullReport = tarifficationService.tarifficate(cdr, report);
           response.getClientReports().add(fullReport);
       }

       return response;
   }

}
