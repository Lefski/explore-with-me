drop table if exists hits;

create table if not exists hits
(
    id        bigint generated by default as identity not null,
    app       varchar(512)                            not null,
    uri       varchar(255)                            not null,
    ip        varchar(255)                            not null,
    timestamp timestamp,
    constraint pk_hit primary key (id)
)


