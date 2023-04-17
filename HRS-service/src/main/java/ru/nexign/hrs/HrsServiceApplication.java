package ru.nexign.hrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.nexign.hrs.service.report.ReportGeneratorService;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.TariffEntity;
import ru.nexign.jpa.request.TarifficationRequest;
import ru.nexign.jpa.service.ClientsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HrsServiceApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(HrsServiceApplication.class, args);

//        final ReportGeneratorService service = applicationContext.getBean(ReportGeneratorService.class);
//
//        List<CallDataRecord> cdrList = new ArrayList<>();
//        cdrList.add(new CallDataRecord("97003646788", CallType.OUTGOING, LocalDateTime.of(2019,
//                Month.MARCH,
//                28,
//                14,
//                33,
//                48), LocalDateTime.of(2019,
//                Month.MARCH,
//                28,
//                14,
//                50,
//                50), new TariffEntity("03",
//                "Perminute",
//                null,
//                null,
//                BigDecimal.valueOf(1.5),
//                null,
//                null,
//                false,
//                false,
//                new ArrayList<>())));
//        var report = service.generateReport(new TarifficationRequest(cdrList)).getClientReports();
//        System.out.println(report.get(0).getCalls().get(0).getCost());
//        System.out.println(report.size());

    }

}
