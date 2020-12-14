-- user data
INSERT INTO user (username, password, email) VALUES("author", "$2a$10$Rm5vX/TTztReM.FQtthbZ.St6GMRgAGyN/2wEFrMLYKEvOPHyY0kG", "author@gmail.com");
INSERT INTO user (username, password, email) VALUES("admin", "$2a$10$APTd6ogky8/3dqbnjvrOm.5MOe0VuknMg9J0R6iJVZC13GE19EN8i", "admin@gmail.com");

-- post data
INSERT INTO post (title, content, user_id) VALUES("test1 title", "test1 content", 1);
INSERT INTO post (title, content, user_id) VALUES("test2 title", "test2 content", 2);
