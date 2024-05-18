create table category
(
    id bigint auto_increment primary key
);

create table customer
(
    id        bigint auto_increment primary key,
    name      varchar(255)              null,
    gender    tinyint(1)                null,
    comment   varchar(255)              null,
    create_by varchar(64)               null,
    create_at timestamp default (now()) null,
    update_by varchar(64)               null,
    update_at timestamp default (now()) null
);