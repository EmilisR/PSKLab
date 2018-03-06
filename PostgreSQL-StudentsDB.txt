CREATE TABLE university (
  id serial PRIMARY KEY,
  title CHARACTER VARYING(100) NOT NULL,
  opt_lock_version INTEGER
);

CREATE TABLE student (
  id serial PRIMARY KEY,
  first_name CHARACTER VARYING(20),
  last_name CHARACTER VARYING(20),
  registration_no CHARACTER VARYING(20) NOT NULL,
  university_id INTEGER,
  opt_lock_version INTEGER,
  FOREIGN KEY (university_id) REFERENCES university (id)
);

CREATE TABLE course (
  id serial PRIMARY KEY,
  name CHARACTER VARYING(100) NOT NULL,
  opt_lock_version INTEGER
);

CREATE TABLE student_course
(
	student_id      INTEGER,
	course_id       INTEGER,
	PRIMARY KEY (student_id, course_id),
	FOREIGN KEY (student_id) REFERENCES student (id),
	FOREIGN KEY (course_id) REFERENCES course (id)
);