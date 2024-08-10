drop table if exists sys_user_role;
drop table if exists sys_user_permission;
drop table if exists sys_role_permission;
drop table if exists sys_user;
drop table if exists sys_role;
drop table if exists sys_permission;

create table sys_user
(
    id            bigint primary key auto_increment,
    username      varchar not null unique,
    password      varchar not null,
    unlocked_time timestamp comment '账号解除锁定的时间',
    enabled       boolean not null default true,
    create_at timestamp default current_timestamp(),
    update_at timestamp default current_timestamp()
);

create table sys_role
(
    name varchar_ignorecase not null unique primary key,
    enabled   boolean   default true,
    create_at timestamp default current_timestamp()
);

create table sys_permission
(
    name varchar_ignorecase not null unique primary key,
    enabled   boolean   default true,
    create_at timestamp default current_timestamp()
);

create table sys_user_role
(
    uid       bigint  not null constraint fk_ur_un references sys_user (id) on delete cascade on update cascade,
    role_name varchar not null constraint fk_ur_rn references sys_role (name) on delete cascade on update cascade,
    create_at timestamp default current_timestamp()
);

create table sys_user_permission
(
    uid             bigint  not null constraint fk_up_un references sys_user (id) on delete cascade on update cascade,
    permission_name varchar not null constraint fk_up_rn references sys_role (name) on delete cascade on update cascade,
    create_at       timestamp default current_timestamp()
);

create table sys_role_permission
(
    role_name       varchar not null constraint fk_rp_rn references sys_role (name) on delete cascade on update cascade,
    permission_name varchar not null constraint fk_rp_pn references sys_permission (name) on delete cascade on update cascade,
    create_at       timestamp default current_timestamp()
)
