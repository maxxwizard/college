DROP TABLE Tags;
DROP TABLE Comments;
DROP TABLE Photos
DROP TABLE Albums;
DROP TABLE Friends;
DROP TABLE Users;

CREATE TABLE Users (
	user_id INT NOT NULL AUTO_INCREMENT,
	email varchar(255) NOT NULL UNIQUE,
	password varchar(255) NOT NULL,
	firstName varchar(255) NOT NULL,
	lastName varchar(255) NOT NULL,
	role_name varchar(255) DEFAULT 'RegisteredUser',
	dob date NOT NULL,
	gender varchar(6),
	home_city varchar(255),
	home_state varchar(255),
	home_country varchar(255),
	addr_city varchar(255),
	addr_state varchar(255),
	addr_country varchar(255),
	eduLevel varchar(255),
	CONSTRAINT users_pk PRIMARY KEY (user_id)
);
		
CREATE TABLE Friends (
	requestor_id INT NOT NULL,
	requestee_id INT NOT NULL,
	PRIMARY KEY (requestor_id, requestee_id),
	CONSTRAINT requestor_id_fk FOREIGN KEY (requestor_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT requestee_id_fk FOREIGN KEY (requestee_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Albums (
	album_id INT NOT NULL AUTO_INCREMENT,
	owner_id INT NOT NULL,
	name varchar(255),
	creationDate TIMESTAMP DEFAULT now(),
	CONSTRAINT albums_pk PRIMARY KEY (album_id),
	CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Photos (
	photo_id INT NOT NULL AUTO_INCREMENT,
	album_id INT NOT NULL,
	caption varchar(255),
	data MEDIUMBLOB NOT NULL,
	thumbnail MEDIUMBLOB NOT NULL,
	filesize INT NOT NULL,
	content_type varchar(45) NOT NULL,
	CONSTRAINT photo_id_pk PRIMARY KEY (photo_id),
	CONSTRAINT album_id_fk FOREIGN KEY (album_id) REFERENCES Albums (album_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Comments (
	comment_id INT NOT NULL AUTO_INCREMENT,
	photo_id INT NOT NULL,
	owner_id INT NOT NULL,
	data varchar(512) NOT NULL,
	date TIMESTAMP DEFAULT now(),
	PRIMARY KEY (comment_id),
	CONSTRAINT photo_id_fk FOREIGN KEY (photo_id) REFERENCES Photos (photo_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT owner_id_fk FOREIGN KEY (owner_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Tags (
	photo_id INT NOT NULL,
	tag_name varchar(45) NOT NULL,
	PRIMARY KEY (photo_id, tag_name),
	CONSTRAINT photo_id_fk FOREIGN KEY (photo_id) REFERENCES Photos (photo_id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Users (email, password, firstName, lastName, role_name, dob, gender,
						home_city, home_state, home_country, addr_city, addr_state, addr_country, eduLevel)
VALUES ('mhuynh@bu.edu', 'berries', 'Matthew', 'Huynh', 'RegisteredUser', '1988-11-22', 'male',
		'Saigon', 'VN', 'Vietnam', 'Boston', 'MA', 'USA', 'College');