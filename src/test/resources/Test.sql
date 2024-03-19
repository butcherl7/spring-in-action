SELECT A.ID,
       A.USERNAME,
       A.PASSWORD,
       A.ENABLED,
       C.NAME    R_NAME,
       C.ENABLED R_ENABLED,
       E.NAME    P_NAME,
       E.ENABLED P_ENABLED
FROM SYS_USER A
         LEFT JOIN SYS_USER_ROLE B ON A.ID = B.UID
         LEFT JOIN SYS_ROLE C ON B.ROLE_NAME = C.NAME AND C.ENABLED = TRUE
         LEFT JOIN SYS_ROLE_PERMISSION D ON C.NAME = D.ROLE_NAME
         LEFT JOIN SYS_PERMISSION E ON D.PERMISSION_NAME = E.NAME AND E.ENABLED = TRUE
WHERE A.USERNAME = 'admin';

SELECT DATEADD('HOUR', 1, CURRENT_TIMESTAMP);

select a.name
from sys_role a,
     sys_user_role b
where a.name = b.role_name
  and a.enabled is true
  and b.uid = 1;

select a.name
from sys_permission a,
     sys_user_permission b
where a.name = b.permission_name
  and a.enabled is true
  and b.uid = 1
union all
select b.name
from sys_role a,
     sys_permission b,
     sys_user_role c,
     sys_role_permission d
where a.name = c.role_name
  and a.name = d.role_name
  and b.name = d.permission_name
  and a.enabled is true
  and b.enabled is true
  and c.uid = 1;

select *
from SYS_PERMISSION;
