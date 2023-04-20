package ru.nexign.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "client_calls")
public class CallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name = "call_type", nullable = false)
    private String callType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "cost", scale = 2)
    private BigDecimal cost;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

    public CallEntity(String callType, LocalDateTime startTime, LocalDateTime endTime, String duration, BigDecimal cost, ReportEntity report) {
        this.callType = callType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.cost = cost;
        this.report = report;
    }
}
