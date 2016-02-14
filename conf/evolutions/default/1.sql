# User schema

# --- !Ups

CREATE TABLE "user" (
    id serial PRIMARY KEY,
    "firstName" varchar(255) NOT NULL,
    "lastName" varchar(255) NOT NULL,
    "fullName" varchar(510) NOT NULL,
    age integer NOT NULL,
    active boolean
);

# --- !Downs

DROP TABLE "user";