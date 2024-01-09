drop table if exists sys_role_permission;
drop table if exists sys_user_role;
drop table if exists sys_user;
drop table if exists sys_role;
drop table if exists sys_permission;

create table sys_user
(
    id          bigint primary key auto_increment,
    username    varchar not null unique,
    password      varchar not null,
    locked        boolean,
    locked_time timestamp,
    enabled       boolean   default true,
    created_time  timestamp default current_timestamp(),
    modified_time timestamp default current_timestamp()
);

create table sys_role
(
    name varchar_ignorecase not null unique primary key,
    enabled      boolean   default true,
    created_time timestamp default current_timestamp()
);

create table sys_permission
(
    name varchar_ignorecase not null unique primary key,
    enabled      boolean   default true,
    created_time timestamp default current_timestamp()
);

create table sys_user_role
(
    uid bigint not null
        constraint fk_ur_un references sys_user (id) on delete cascade on update cascade,
    role_name    varchar not null
        constraint ur_ur_rn references sys_role (name) on delete cascade on update cascade,
    created_time timestamp default current_timestamp()
);

create table sys_role_permission
(

    role_name       varchar not null
        constraint fk_rp_rn references sys_role (name) on delete cascade on update cascade,
    permission_name varchar not null
        constraint fk_rp_pn references sys_permission (name) on delete cascade on update cascade,
    created_time    timestamp default current_timestamp()
)
