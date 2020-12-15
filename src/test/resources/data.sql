-- truncate tables
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE user_role;
TRUNCATE TABLE post; 
TRUNCATE TABLE user;
SET FOREIGN_KEY_CHECKS=1;

-- user data
INSERT INTO user (id, username, password, email, is_account_non_locked, created_on) VALUES (1, 'author', '$2a$10$Rm5vX/TTztReM.FQtthbZ.St6GMRgAGyN/2wEFrMLYKEvOPHyY0kG', 'author@gmail.com', true, '2020-12-15 16:39:38');
INSERT INTO user (id, username, password, email, is_account_non_locked, created_on) VALUES (2, 'admin', '$2a$10$Rm5vX/TTztReM.FQtthbZ.St6GMRgAGyN/2wEFrMLYKEvOPHyY0kG', 'admin@gmail.com', true, '2020-12-15 16:39:38');

-- user_role data
INSERT INTO user_role (user_id, role) VALUES (1, 'ROLE_AUTHOR');
INSERT INTO user_role (user_id, role) VALUES (2, 'ROLE_ADMINISTRATOR');

-- post data
INSERT INTO post (title, content, user_id) VALUES ('test1 title', 'test1 content', 1);
INSERT INTO post (title, content, user_id) VALUES ('test2 title', 'test2 content', 2);
