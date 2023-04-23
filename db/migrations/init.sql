create table public.tariffs
(
    id                  varchar(255) not null
        primary key,
    first_minute_limit  integer,
    first_minute_price  numeric(19, 2),
    fix_price           numeric(19, 2),
    free_minute_limit   integer,
    is_for_clients_free boolean      not null,
    is_incoming_free    boolean      not null,
    name                varchar(255) not null
        constraint uk_lt51ssbtjwb2x833uph2ngfag
            unique,
    next_minute_price   numeric(19, 2)
)
    using ???;

alter table public.tariffs
    owner to service;

create table public.clients
(
    id           bigserial
        primary key,
    balance      numeric(19, 2) not null,
    phone_number varchar(255)   not null
        constraint uk_bt1ji0od8t2mhp0thot6pod8u
            unique,
    tariff_id    varchar(255)   not null
        constraint fkm1kg1pe0ij97a1c4r9hk0biw1
            references public.tariffs
)
    using ???;

alter table public.clients
    owner to service;

create table public.reports
(
    id            bigserial
        primary key,
    monetary_unit varchar(255) not null,
    total_cost    numeric(19, 2),
    client_id     bigint       not null
        constraint fkmiqk34gfam6emk63vq844fem2
            references public.clients
)
    using ???;

alter table public.reports
    owner to service;

create table public.client_calls
(
    id         bigserial
        primary key,
    call_type  varchar(255) not null,
    cost       numeric(19, 2),
    duration   varchar(255) not null,
    end_time   timestamp    not null,
    start_time timestamp    not null,
    report_id  bigint       not null
        constraint fk32bldqpjd6205vrqan0k9s5ns
            references public.reports
)
    using ???;

alter table public.client_calls
    owner to service;

create table public.users
(
    id           bigserial
        primary key,
    password     varchar(255) not null,
    phone_number varchar(255) not null
        constraint uk_9q63snka3mdh91as4io72espi
            unique,
    role         varchar(255) not null,
    username     varchar(255) not null
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique
)
    using ???;

alter table public.users
    owner to service;
