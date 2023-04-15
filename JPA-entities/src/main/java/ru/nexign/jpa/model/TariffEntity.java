package ru.nexign.jpa.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tariffs")
public class TariffEntity {
    @Id
    @Column(name= "id")
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "fix_price", scale = 2)
    private BigDecimal fixPrice;

    @Column(name = "free_minute_limit")
    private Integer freeMinuteLimit;

    @Column(name = "first_minute_price", scale = 2)
    private BigDecimal firstMinutePrice;

    @Column(name = "first_minute_limit")
    private Integer firstMinuteLimit;

    @Column(name = "next_minute_price", scale = 2)
    private BigDecimal nextMinutePrice;

    @Column(name = "is_incoming_free", nullable = false)
    private Boolean isIncomingFree;

    @Column(name = "is_for_clients_free", nullable = false)
    private Boolean isForClientsFree;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tariff")
    private List<ClientEntity> clients;
}
