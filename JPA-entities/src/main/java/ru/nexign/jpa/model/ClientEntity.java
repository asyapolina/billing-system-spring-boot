package ru.nexign.jpa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Long id;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "balance", nullable = false, scale = 2)
    private BigDecimal balance;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private TariffEntity tariff;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<CallEntity> calls;

    public ClientEntity(String phoneNumber, BigDecimal balance, TariffEntity tariff, List<CallEntity> calls) {
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.tariff = tariff;
        this.calls = calls;
    }
}
