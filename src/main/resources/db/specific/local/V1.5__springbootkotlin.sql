INSERT INTO public.course (id, created_at, icon_url, lectures_count, long_description, name, resources, short_description, slug, status, thumbnail_url, trailer_url, updated_at, author_id, difficulty_id) VALUES (nextval('course_id_seq'), '2022-09-25 10:26:15.808704 +00:00', 'http://streetofcode.sk/wp-content/uploads/2022/09/finalIconUrl.png', 41, 'TODO', 'Spring Boot - Kotlin', null, 'Nauč sa veľmi žiadaný backendový framework v spolupráci s Kotlinom', 'spring-boot-kotlin', 'DRAFT', '', '746275268', '2022-09-25 10:55:33.505901 +00:00', 1, 2);

INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 1, '2022-09-25 10:27:01.986656 +00:00', 'Úvod do kurzu', '2022-09-25 10:27:01.986668 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 2, '2022-09-25 10:27:47.342024 +00:00', 'Kotlin a Spring Boot', '2022-09-25 10:27:47.342038 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 3, '2022-09-25 10:27:57.536224 +00:00', 'Vytvorenie projektu', '2022-09-25 10:27:57.536237 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 4, '2022-09-25 10:28:16.206671 +00:00', 'Návrh projektu Knižnica', '2022-09-25 10:28:16.206684 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 5, '2022-09-25 10:28:27.081058 +00:00', 'Definujeme doménu a API', '2022-09-25 10:28:27.081080 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 6, '2022-09-25 10:28:45.858858 +00:00', 'Vytvárame databázu', '2022-09-25 10:28:45.858869 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 7, '2022-09-25 10:29:01.703577 +00:00', 'Implementácia API pomocou JDBC', '2022-09-25 10:29:01.703587 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 8, '2022-09-25 10:29:09.602430 +00:00', 'Implementácia API pomocou JPA', '2022-09-25 10:29:09.602440 +00:00', 2);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (nextval('chapter_id_seq'), 9, '2022-09-25 10:29:16.915790 +00:00', 'Záver', '2022-09-25 10:29:16.915804 +00:00', 2);

INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:30:42.175269 +00:00', 1, 'O čom je tento kurz', '2022-09-25 10:30:42.175285 +00:00', 410, '746275268', 17);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:31:21.037634 +00:00', 2, 'Čo spolu vytvoríme', '2022-09-25 10:31:21.037647 +00:00', 399, '746274897', 17);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:31:56.919332 +00:00', 1, 'Prečo Kotlin', '2022-09-25 10:31:56.919348 +00:00', 492, '746276139', 18);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:32:12.105876 +00:00', 2, 'Čo je to Spring Boot', '2022-09-25 10:32:12.105886 +00:00', 339, '746275795', 18);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:32:29.643582 +00:00', 3, 'Čo je to dependency injection', '2022-09-25 10:32:29.643598 +00:00', 413, '746275742', 18);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:33:25.174807 +00:00', 1, 'Vytvárame Spring boot projekt', '2022-09-25 10:33:25.174825 +00:00', 466, '746277815', 19);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:36:04.783399 +00:00', 1, 'Vytvorenie doménových tried', '2022-09-25 10:36:04.783429 +00:00', 189, '746280850', 21);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:36:46.059072 +00:00', 1, 'Pridanie H2 databázy', '2022-09-25 10:36:46.059085 +00:00', 359, '746281204', 22);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:33:41.420553 +00:00', 2, 'Orientácia v projekte', '2022-09-25 10:33:41.420581 +00:00', 450, '746276571', 19);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:34:09.088521 +00:00', 3, 'Vytvárame Rest HelloController', '2022-09-25 10:34:09.088534 +00:00', 577, '746277271', 19);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:34:32.269682 +00:00', 4, 'Vytvárame Spring Beany', '2022-09-25 10:34:32.269698 +00:00', 430, '746276892', 19);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:35:05.535818 +00:00', 1, 'Čo je to projekt Knižnica', '2022-09-25 10:35:05.535838 +00:00', 134, '746278149', 20);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:35:17.424637 +00:00', 2, 'Návrh aplikácie', '2022-09-25 10:35:17.424647 +00:00', 296, '746278255', 20);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:35:28.779471 +00:00', 3, 'Návrh databázy', '2022-09-25 10:35:28.779487 +00:00', 411, '746278457', 20);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:36:17.488429 +00:00', 2, 'Vytvorenie API servicov', '2022-09-25 10:36:17.488441 +00:00', 685, '746279175', 21);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), '## Definícia schémy v schema.sql

```
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `author_id` bigint NOT NULL,
  `category_id` bigint,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `author_id_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
  CONSTRAINT `category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
);
```

## Pridanie dát do data.sql

```
create sequence author_id_seq start with 1 increment by 1;
create sequence category_id_seq start with 1 increment by 1;
create sequence book_id_seq start with 1 increment by 1;

-- id, name, surname
INSERT INTO author VALUES
(next value for author_id_seq, ''J.K.'', ''Rowling''),
(next value for author_id_seq, ''J.R.R.'', ''Tolkien''),
(next value for author_id_seq, ''Daniel'', ''Kahneman''),
(next value for author_id_seq, ''Robert C.'', ''Martin'');
-- id, name, description
INSERT INTO category VALUES
(next value for category_id_seq, ''Fantasy'', ''Fiction literature''),
(next value for category_id_seq, ''Nonfiction'', ''Nonfiction literature'');
-- id, name, description, created_at, author_id, category_id
INSERT INTO book VALUES
(next value for book_id_seq, 1, 1, ''Harry Potter'', ''Story about young wizard'', CURRENT_TIMESTAMP),
(next value for book_id_seq, 2, 1, ''The Fellowship of the ring'', ''One Ring to rule them all'', CURRENT_TIMESTAMP),
(next value for book_id_seq, 3, 2, ''Thinking, Fast and Slow'', ''System 1 and System 2'', CURRENT_TIMESTAMP),
(next value for book_id_seq, 4, null, ''Clean Code'', ''How to write clean code'', CURRENT_TIMESTAMP);
```', '2022-09-25 10:40:21.329068 +00:00', 2, 'Vytvorenie DB schémy a pridanie dát', '2022-09-25 10:40:21.329086 +00:00', 497, '746281934', 22);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:42:13.620244 +00:00', 1, 'Author - Nachystajme si triedy', '2022-09-25 10:42:13.620257 +00:00', 415, '746296800', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:42:42.367602 +00:00', 2, 'AuthorRowMapper', '2022-09-25 10:42:42.367614 +00:00', 384, '746295700', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:43:16.529953 +00:00', 3, 'AuthorJdbcRepository - getAuthor', '2022-09-25 10:43:16.529968 +00:00', 783, '746291749', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:43:34.862287 +00:00', 4, 'AuthorJdbcRepository - zvyšok', '2022-09-25 10:43:34.862302 +00:00', 856, '746294023', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:44:09.986663 +00:00', 5, 'AuthorServiceJdbcImpl', '2022-09-25 10:44:09.986678 +00:00', 250, '746296265', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:44:29.486455 +00:00', 6, 'AuthorController', '2022-09-25 10:44:29.486465 +00:00', 1070, '746288888', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:44:55.797153 +00:00', 7, 'Author - integračné testy', '2022-09-25 10:44:55.797168 +00:00', 1883, '746283457', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:45:43.022938 +00:00', 8, 'Implementujeme CategoryService', '2022-09-25 10:45:43.022952 +00:00', 1219, '748562135', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:45:58.745614 +00:00', 9, 'CategoryController', '2022-09-25 10:45:58.745628 +00:00', 508, '748561450', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:46:15.258305 +00:00', 10, 'Category - integračné testy', '2022-09-25 10:46:15.258320 +00:00', 1101, '748560008', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:46:47.990927 +00:00', 11, 'Implementujeme BookService', '2022-09-25 10:46:47.990942 +00:00', 1855, '748557571', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:47:04.062344 +00:00', 12, 'BookController', '2022-09-25 10:47:04.062357 +00:00', 863, '748556544', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:47:17.457135 +00:00', 13, 'Book - integračné testy', '2022-09-25 10:47:17.457150 +00:00', 663, '748554887', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:47:43.502540 +00:00', 14, 'Doplnenie chýbajúcich funkčností', '2022-09-25 10:47:43.502555 +00:00', 793, '746297424', 23);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:49:18.360354 +00:00', 1, 'Vytvorenie entít', '2022-09-25 10:49:18.360366 +00:00', 2241, '748583637', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:49:33.145734 +00:00', 2, 'Vytvorenie repositories', '2022-09-25 10:49:33.145749 +00:00', 231, '748588223', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:50:18.954403 +00:00', 3, 'Implementujeme AuthorService', '2022-09-25 10:50:18.954419 +00:00', 472, '748579732', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:50:46.111586 +00:00', 4, 'Máme problém - profily pomôžu', '2022-09-25 10:50:46.111603 +00:00', 1052, '748586659', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:51:08.763969 +00:00', 5, 'Implementujeme CategoryService', '2022-09-25 10:51:08.763986 +00:00', 577, '748568607', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:51:30.241313 +00:00', 6, 'Implementujeme BookService - part 1', '2022-09-25 10:51:30.241328 +00:00', 625, '748581613', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:51:44.508239 +00:00', 7, 'Implementujeme BookService - part 7', '2022-09-25 10:51:44.508263 +00:00', 702, '748582646', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:52:17.509361 +00:00', 1, 'JDBC vs JPA', '2022-09-25 10:52:17.509372 +00:00', 720, '748572105', 25);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:52:50.903280 +00:00', 3, 'Záverečný odkaz', '2022-09-25 10:52:50.903295 +00:00', 93, '748588562', 25);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 10:54:02.099642 +00:00', 8, 'Doplnenie chýbajúcich funkčností', '2022-09-25 10:54:02.099658 +00:00', 770, '748575593', 24);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (nextval('lecture_id_seq'), null, '2022-09-25 12:36:35.274958 +00:00', 2, 'Nápady na nové funkcionality', '2022-09-25 12:36:35.274977 +00:00', 530, '753509580', 25);
