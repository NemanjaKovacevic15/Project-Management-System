
INSERT INTO User(email,username, password, firstName, lastName)
VALUES ('nati@live.com','nati','$2a$10$pHJRgyB/nIqivZ34DvrnNOk6UGHSI08hI0MddkMJaS218gUwML08a', 'Nati','Natasha');

INSERT INTO User_UserRole (user_id, userRole_name)
  SELECT user.id, role.name FROM User user, UserRole role
  where user.username='nati' and role.name='ADMIN';

-- ----

INSERT INTO User(email,username, password, firstName, lastName)
VALUES ('nemanja@live.com','GET','$2a$10$pHJRgyB/nIqivZ34DvrnNOk6UGHSI08hI0MddkMJaS218gUwML08a', 'Nemanja','Milojevic');

INSERT INTO User_UserRole (user_id, userRole_name)
  SELECT user.id, role.name FROM User user, UserRole role
  where user.username='GET' and role.name='ADMIN';
-- -- --------------------------------------
-- -- --------------------------------------
-- -- -------- Insert User_UserRole -------
-- -- --------------------------------------
-- -- --------------------------------------
INSERT INTO nemanja_db.UserRole (name, description) VALUES ('ADMIN', 'Admin koji moze da kreira sve');
INSERT INTO nemanja_db.UserRole (name, description) VALUES ('DEVELOPER', 'Developer user');
INSERT INTO nemanja_db.UserRole (name, description) VALUES ('PROJECT_MANAGER', 'Project manager');

