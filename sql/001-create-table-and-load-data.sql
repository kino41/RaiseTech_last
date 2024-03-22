DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id int unsigned AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  birthdate VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO users (name, birthdate) VALUES ("suzuki", "1973/07/22");
INSERT INTO users (name, birthdate) VALUES ("kato", "1960/03/15");
InSERT INTO users (name, birthdate) VALUES ("tanaka", "1991/12/08");
