drop table if exists
    users,
    categories,
    events,
    participation_requests,
    compilations,
    compilations_events,
    comments,
    cascade;

create table if not exists users
(
    id    bigint generated by default as identity not null,
    name  varchar(255)                            not null,
    email varchar(255)                            not null,
    constraint pk_user primary key (id),
    constraint uq_user_email unique (email)
);


create table if not exists categories
(
    id   bigint generated by default as identity not null,
    name varchar(50)                             not null,
    constraint pk_category primary key (id),
    constraint uq_category_name unique (name)
);

create table if not exists events
(
    id                 bigint generated by default as identity not null,
    state              varchar(255)                            not null,
    annotation         varchar(2000)                           not null,
    category_id        bigint references categories (id)       not null,
    description        varchar(7000)                           not null,
    event_date         timestamp                               not null,
    lat                real                                    not null,
    lon                real                                    not null,
    paid               bool                                    not null,
    participants_limit integer                                 not null,
    request_moderation bool                                    not null,
    title              varchar(120)                            not null,
    created_on         timestamp default current_timestamp     not null,
    initiator_id       integer                                 not null,
    published_on       timestamp,
    constraint pk_events primary key (id),
    constraint fk_category_id foreign key (category_id) references categories (id) on delete restrict
);

create table if not exists participation_requests
(
    id           bigint generated by default as identity not null,
    created      timestamp default current_timestamp     not null,
    event_id     bigint references events (id)           not null,
    requester_id bigint references users (id)            not null,
    status       varchar(255)                            not null,
    constraint pk_requests primary key (id)
);

create table if not exists compilations
(
    id     bigint generated by default as identity not null,
    pinned boolean                                 not null,
    title  varchar(50)                             not null,
    constraint pk_compilations primary key (id)
);

create table if not exists compilations_events
(
    compilation_id bigint not null,
    event_id       bigint not null,
    constraint fk_compilation_id foreign key (compilation_id) references compilations (id),
    constraint fk_event_id foreign key (event_id) references events (id)
);

create table if not exists comments
(
    id           bigint generated by default as identity not null,
    event_id     bigint                                  not null,
    creator_id   bigint                                  not null,
    comment_text text                                    not null,
    status       varchar(255)                            not null,
    created      timestamp default current_timestamp     not null,
    updated      timestamp,
    constraint pk_comments primary key (id),
    constraint fk_event_id foreign key (event_id) references events (id),
    constraint fk_creator_id foreign key (creator_id) references users (id)
);





