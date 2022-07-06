CREATE TABLE users (
	user_id          SERIAL NOT NULL PRIMARY KEY UNIQUE,
	first_name       varchar(50),
	last_name        varchar(50),
	username        varchar(50) NOT NULL UNIQUE,
	email           varchar(50) UNIQUE,
	usertype     varchar(30),
	password		varchar not NULL,
	salt			bytea,
	timestamp    TIMESTAMPTZ
);

CREATE TABLE students (
	user_id          INT NOT NULL PRIMARY KEY UNIQUE,
	major             varchar(30),
	gpa                decimal,
	CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE faculty (
	user_id          INT NOT NULL PRIMARY KEY UNIQUE,
	department  varchar(50),
	CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE courses (
    course_id          SERIAL NOT NULL PRIMARY KEY UNIQUE,
    course_name       varchar(50) UNIQUE,
    semester        varchar(50),
    is_available    bool,
    capacity        integer NOT NULL,
    size        integer NOT NULL
);

CREATE TABLE courses_users (
		course_id int NOT NULL,
		user_id int NOT NULL,
		CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT courses_fk FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE
);