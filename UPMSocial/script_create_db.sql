-- Host: sql8.freemysqlhosting.net
-- Database name: sql8115240
-- Database user: sql8115240
-- Database password: wEShxsCLq1
-- Port number: 3306

CREATE DATABASE IF NOT EXISTS UPMSocial;
USE UPMSocial;

DROP TABLE IF EXISTS POSTS;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
    username VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) DEFAULT NULL,
    phone INT DEFAULT NULL,
    email VARCHAR(30) DEFAULT NULL,
    address VARCHAR(50) DEFAULT NULL,
    register_date DATE NOT NULL,
    PRIMARY KEY (username)
)  ENGINE=INNODB;

DELIMITER //
CREATE TRIGGER `register_date_user` BEFORE INSERT ON  `USERS` 
	FOR EACH ROW BEGIN
		IF NEW.register_date IS NULL THEN
			SET NEW.register_date = NOW();
		END IF;
	END;//
DELIMITER ;

        
CREATE TABLE POSTS (
    id INT NOT NULL AUTO_INCREMENT,
    author_username VARCHAR(50) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    creation_date DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_author_username_USERS FOREIGN KEY (author_username)
        REFERENCES USERS (username)
        ON UPDATE CASCADE ON DELETE CASCADE
)  ENGINE=INNODB;

CREATE TABLE FRIENDS (
    username VARCHAR(50) NOT NULL,
    friend VARCHAR(50) NOT NULL,
    PRIMARY KEY (username , friend),
    CONSTRAINT fk_username_USERS FOREIGN KEY (username)
        REFERENCES USERS (username)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_friend_USERS FOREIGN KEY (friend)
        REFERENCES USERS (username)
        ON UPDATE CASCADE ON DELETE CASCADE
)  ENGINE=INNODB;

BEGIN;
INSERT INTO USERS(username, first_name, register_date) 
VALUES 
('v130111','Sergio','2009-01-24'), 
('v130110','Rafael', CURRENT_DATE()), 
('v130109','Roberto','2012-07-13'), 
('v130108','David','2001-04-20'), 
('v130107','Carlos','2013-01-01');
COMMIT;

BEGIN;
INSERT INTO POSTS 
VALUES 
('1','v130111','El mundo es un lugar mejor','2013-05-17'), 
('2','v130111','En un lugar de la mancha...','2014-03-25'), 
('3','v130109','En una galaxia muy lejana...',NOW()), 
('4','v130108','Mayday mayday, necesitamos ayuda','2011-09-28'), 
('5','v130107','Caminante, son tus huellas
el camino y nada más;
Caminante, no hay camino,
se hace camino al andar.
Al andar se hace el camino,
y al volver la vista atrás
se ve la senda que nunca
se ha de volver a pisar.
Caminante no hay camino
sino estelas en la mar.','2013-12-31');
COMMIT;

BEGIN;
INSERT INTO FRIENDS 
VALUES 
('v130111','v130107'), 
('v130111','v130109'), 
('v130107','v130109'), 
('v130108','v130110'), 
('v130108','v130107');
COMMIT;

update USERS
set phone=910245356
where username=v130107;

update USERS
set email='emaildeprueba@gmail.com'
where username=v130108;

update USERS
set address='C/Montepríncipe 210'
where username=v130109;

update USERS
set last_name='Arguiñano'
where username=v130110;

select * from FRIENDS;
select * from USERS;
select * from POSTS;
show grants for 'upmsocial'@'%';
grant all privileges on upmsocial.* to 'upmsocial'@'%';