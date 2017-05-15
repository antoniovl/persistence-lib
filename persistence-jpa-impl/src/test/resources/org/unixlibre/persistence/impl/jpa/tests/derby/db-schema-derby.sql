CREATE TABLE authors (
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  name VARCHAR(50) NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  email VARCHAR(100),
  PRIMARY KEY(id)
);

CREATE TABLE articles (
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  author_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content CLOB(50k) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  published_at TIMESTAMP,
  PRIMARY KEY (id)
);

ALTER TABLE articles ADD FOREIGN KEY (author_id) REFERENCES authors(id);

INSERT INTO authors (name, birth_date, email) VALUES ('Author1', '1970-01-01 12:00:00', 'author1@gmail.com');
INSERT INTO authors (name, birth_date, email) VALUES ('Author2', '1980-01-01 12:00:00', 'author2@gmail.com');

INSERT INTO articles (author_id, title, content, created_at)
  VALUES (1, 'First Artice', 'This is my first article', CURRENT_TIMESTAMP);
INSERT INTO articles (author_id, title, content, created_at)
  VALUES (1, 'Second Artice', 'This is my second article', CURRENT_TIMESTAMP);
