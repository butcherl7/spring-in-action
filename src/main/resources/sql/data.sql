insert into sys_user(username, password)
values ('admin', '123456'),
       ('tom', '123456');

insert into sys_role(name)
values ('scholar'),
       ('farmer'),
       ('artisan'),
       ('merchant');

insert into sys_user_role(username, role_name)
values ('tom', 'scholar');