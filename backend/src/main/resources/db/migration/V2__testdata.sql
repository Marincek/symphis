-- "username":"marincek", "password":"changeit"
INSERT INTO USERS VALUES (101,'some@email.com', 'Jan', 'Marincek', '$2a$10$Pjris/x3SkXUK/sG1JfjruTGtgRjQZVVZR.PueiNvAdIQUXjsn0Q2','marincek', '50ef9063-b2fe-4136-a6ac-f2d412d9ea25' );

INSERT INTO LINKS VALUES (301, 'https://dzone.com/articles/spring-boot-rest-service-1' , 101);
INSERT INTO TAGS VALUES (301, 'spring' );
INSERT INTO TAGS VALUES (301, 'java' );
INSERT INTO TAGS VALUES (301, 'boot' );
INSERT INTO TAGS VALUES (301, 'rest' );
INSERT INTO TAGS VALUES (301, 'data' );

INSERT INTO LINKS VALUES (302, 'https://examples.javacodegeeks.com/core-java/java-state-design-pattern-example/' , 101);
INSERT INTO TAGS VALUES (302, 'pattern' );
INSERT INTO TAGS VALUES (302, 'java' );
INSERT INTO TAGS VALUES (302, 'design' );
INSERT INTO TAGS VALUES (302, 'state' );