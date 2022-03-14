INSERT INTO author (id, name, description, url) VALUES
(author_id_seq.nextval, 'Jakub', 'Jahič', 'http://streetofcode.sk/wp-content/uploads/2020/03/00100lPORTRAIT_00100_BURST20190815135005128_COVER1.jpg' ),
(author_id_seq.nextval, 'Gabo', 'Kerekeš', 'url' );

INSERT INTO difficulty (id, name, description) VALUES
(difficulty_id_seq.nextval, 'Beginner', 'Jednoduche'),
(difficulty_id_seq.nextval, 'Intermediate', 'Pokrocile');


INSERT INTO course (id, author_id, difficulty_id, name, short_description, long_description, image_url, status, created_at, updated_at) VALUES
(course_id_seq.nextval, 1, 1, 'Informatika 101', 'Uvod do informatiky', 'Tento kurz ta nauci lorem ipsum', 'http://streetofcode.sk/wp-content/uploads/2020/03/screen-coding-programming-web-design-2061168.jpg', 'PUBLIC', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(course_id_seq.nextval, 2, 2, 'Kryptografia', 'Uvod do kryptografie', 'Jeden z tych lepsich kurzov o kryptografii', null, 'DRAFT', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

INSERT INTO chapter (id, course_id, name, chapter_order, created_at, updated_at) VALUES
(chapter_id_seq.nextval, 1, 'Uvodna kapitolka do informatiky', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(chapter_id_seq.nextval, 1, 'Ako vznikol pocitac', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(chapter_id_seq.nextval, 2, 'Uvodna kapitolka do informatiky', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(chapter_id_seq.nextval, 2, 'Dalsia kapitolka o informatike', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' );


INSERT INTO lecture (id, chapter_id, name, content, video_url, video_duration_seconds, lecture_order, created_at, updated_at) VALUES
(lecture_id_seq.nextval, 1, 'Part 1', STRINGDECODE('# Library project for Kotlin Spring Boot online course\n**Application manages books, authors and book categories.** \n\n![alt text](http://streetofcode.sk/wp-content/uploads/2019/11/library-object-model.png)\n![alt text](http://streetofcode.sk/wp-content/uploads/2019/11/library-data-model.png)\n\n**What''s part of the project:**\n* JDK 11\n* Kotlin\n* JDBCTemplate\n* JPA Hibernate\n* H2 in-memory database\n* CRUD operations\n* REST API\n* Swagger\n* Integration tests\n'), 'https://www.youtube.com/embed/z1At9Jk4sqE', 124, 1, '007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 1, 'Part 2', null, 'https://www.youtube.com/embed/z1At9Jk4sqE', 124, 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(lecture_id_seq.nextval, 1, 'Part 3', STRINGDECODE('# Library project for Kotlin Spring Boot online course\n**Application manages books, authors and book categories.** \n\n![alt text](http://streetofcode.sk/wp-content/uploads/2019/11/library-object-model.png)\n![alt text](http://streetofcode.sk/wp-content/uploads/2019/11/library-data-model.png)\n\n**What''s part of the project:**\n* JDK 11\n* Kotlin\n* JDBCTemplate\n* JPA Hibernate\n* H2 in-memory database\n* CRUD operations\n* REST API\n* Swagger\n* Integration tests\n'), null, 0, 3, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 2, 'Part 1', 'Mega kontent', null, 12050, 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 2, 'Part 2', 'Mega kontent druhy', null, 5000, 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 3, 'Part 1', 'Mega kontent', null, 5000, 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 3, 'Part 2', 'Mega kontent druhy', null, 130, 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(lecture_id_seq.nextval, 3, 'Part 3', 'Mega kontent treti', null, 5000, 3, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 4, 'Part 1', 'Mega kontent', null, 5000, 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 4, 'Part 2', 'Mega kontent druhy', null, 5000, 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

INSERT INTO course_review (id, user_id, course_id, rating, text, user_name, created_at, updated_at) VALUES
(course_review_id_seq.nextval, 'bb9e0186-aaae-11eb-bcbc-0242ac130002', 1, 5, 'Mega kurz', 'Gabko', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'),
(course_review_id_seq.nextval, 'c2c73db0-aaae-11eb-bcbc-0242ac130002', 1, 0, 'Na nic kurz', 'Kubko', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'),

(course_review_id_seq.nextval, 'bb9e0186-aaae-11eb-bcbc-0242ac130002', 2, 3, 'Priemerny kurz', 'Kubko', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'),
(course_review_id_seq.nextval, 'c2c73db0-aaae-11eb-bcbc-0242ac130002', 2, 1, 'Nie najhorsie, ale dalo by sa aj lepsie', 'Gabko', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

UPDATE course set lectures_count = 5 where id = 1;
UPDATE course set lectures_count = 5 where id = 2;

INSERT INTO lecture_comment(id, user_id, lecture_id, user_name, comment_text, created_at, updated_at) VALUES
(lecture_comment_id_seq.nextval, 'e6bff8b2-7376-4e19-a1e7-c29f1f185faf', 1, 'jozko', 'toto je super lekcia', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(lecture_comment_id_seq.nextval, '93c6d283-8d2d-495b-b08f-9a18831d1f5e', 1, 'janka', 'parada', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' );

INSERT INTO quiz(id, lecture_id, title, subtitle, created_at, finished_message) VALUES
(quiz_id_seq.nextval, 1, 'Kvizik', 'Tu sa naucis matiku', '2007-12-03T10:15:30+01:00', 'Spravne!!!');

INSERT INTO quiz_question(id, quiz_id, question_order, text, type) VALUES
(quiz_question_id_seq.nextval, 1, 0, '2+2?', 'SINGLE_CHOICE'),
(quiz_question_id_seq.nextval, 1, 1, '2*2', 'SINGLE_CHOICE'),
(quiz_question_id_seq.nextval, 1, 2, '8-4', 'MULTIPLE_CHOICE');

INSERT INTO quiz_question_answer(id, quiz_question_id, text, is_correct) VALUES
(quiz_question_answer_id_seq.nextval, 1, '1', false),
(quiz_question_answer_id_seq.nextval, 1, '2', true),
(quiz_question_answer_id_seq.nextval, 1, '3', false),
(quiz_question_answer_id_seq.nextval, 1, '4', false),
(quiz_question_answer_id_seq.nextval, 2, '1', false),
(quiz_question_answer_id_seq.nextval, 2, '2', false),
(quiz_question_answer_id_seq.nextval, 2, '3', false),
(quiz_question_answer_id_seq.nextval, 2, '4', true),
(quiz_question_answer_id_seq.nextval, 3, '1', false),
(quiz_question_answer_id_seq.nextval, 3, '2', false),
(quiz_question_answer_id_seq.nextval, 3, '+4', true),
(quiz_question_answer_id_seq.nextval, 3, '4', true);

INSERT INTO quiz_question_user_answer(id, question_id, answer_id, user_id, created_at, try_count) VALUES
(quiz_question_user_answer_id_seq.nextval, 1, 2, 'e6bff8b2-7376-4e19-a1e7-c29f1f185faf', '2007-12-03T10:15:30+01:00', 3);
