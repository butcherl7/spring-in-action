insert into sys_user(username, password)
values ('admin', '123456'),
       ('tom', '123456'),
       ('alice', '123456');

insert into sys_role(name, enabled)
values ('admin', true),
       ('people', true);

insert into sys_permission(name, enabled)
values ('book:read:*', true),
       ('book:borrow:*', true),
       ('book:read:home', true),
       ('book:borrow:home', true);

insert into sys_user_role(uid, role_name)
values (1, 'admin'),
       (1, 'people'),
       (2, 'people');

insert into sys_role_permission (role_name, permission_name)
values ('admin', 'book:read:*'),
       ('admin', 'book:borrow:*'),
       ('people', 'book:read:home'),
       ('people', 'book:borrow:home');