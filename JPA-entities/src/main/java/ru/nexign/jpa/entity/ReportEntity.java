package ru.nexign.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "monetary_unit", nullable = false)
    private String monetaryUnit;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "report", cascade = CascadeType.ALL)
    private List<CallEntity> calls;

    public ReportEntity(BigDecimal totalCost, String monetaryUnit, ClientEntity client) {
        this.totalCost = totalCost;
        this.monetaryUnit = monetaryUnit;
        this.client = client;
        this.calls = new ArrayList<>();
    }

}
