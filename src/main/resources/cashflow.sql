-- auto-generated definition
create table account
(
    id            bigserial
        primary key,
    creation_date date         not null,
    name          varchar(255) not null
);

alter table account
    owner to cashflow;

-- auto-generated definition
create table category
(
    id            bigserial
        primary key,
    creation_date date         not null,
    name          varchar(255) not null
);

alter table category
    owner to cashflow;

-- auto-generated definition
create table transaction
(
    id                bigserial
        primary key,
    amount            real not null,
    date              date not null,
    description       varchar(255),
    account_id        bigint
        constraint fk6g20fcr3bhr6bihgy24rq1r1b
            references account,
    category_id       bigint
        constraint fkgik7ruym8r1n4xngrclc6kiih
            references category,
    before_conversion real,
    change_rate       real,
    currency          varchar(255),
    holiday           varchar(255),
    is_common         boolean,
    month             varchar(255),
    nbr_of_actions    integer,
    ticker            varchar(255),
    week_number       integer,
    year              integer,
    reference         bigint
);

alter table transaction
    owner to cashflow;

