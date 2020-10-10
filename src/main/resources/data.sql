INSERT INTO author (id, name, description, url) VALUES
(author_id_seq.nextval, 'Jakub', 'Jahič', 'http://streetofcode.sk/wp-content/uploads/2020/03/00100lPORTRAIT_00100_BURST20190815135005128_COVER1.jpg' ),
(author_id_seq.nextval, 'Gabo', 'Kerekeš', 'url' );


INSERT INTO difficulty (id, name, description, difficulty_order) VALUES
(difficulty_id_seq.nextval, 'Beginner', 'Jednoduche', 1 ),
(difficulty_id_seq.nextval, 'Intermediate', 'Pokrocile', 2 );


INSERT INTO course (id, author_id, difficulty_id, name, short_description, long_description, created_at, updated_at) VALUES
(course_id_seq.nextval, 1, 1, 'Informatika 101', 'Uvod do informatiky', 'Tento kurz ta nauci lorem ipsum', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(course_id_seq.nextval, 2, 2, 'Kryptografia', 'Uvod do kryptografie', 'Jeden z tych lepsich kurzov o kryptografii', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

INSERT INTO chapter (id, course_id, name, chapter_order, created_at, updated_at) VALUES
(chapter_id_seq.nextval, 1, 'Uvodna kapitolka do informatiky', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(chapter_id_seq.nextval, 1, 'Ako vznikol pocitac', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(chapter_id_seq.nextval, 2, 'Uvodna kapitolka do informatiky', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(chapter_id_seq.nextval, 2, 'Dalsia kapitolka o informatike', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' );


INSERT INTO lecture (id, chapter_id, name, content, lecture_order, created_at, updated_at) VALUES
(lecture_id_seq.nextval, 1, 'Part 1', 'Mega kontent', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 1, 'Part 2', 'Mega kontent druhy', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 2, 'Part 1', 'Mega kontent', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 2, 'Part 2', 'Mega kontent druhy', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 3, 'Part 1', 'Mega kontent', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 3, 'Part 2', 'Mega kontent druhy', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(lecture_id_seq.nextval, 3, 'Part 3', 'Mega kontent treti', 3, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),

(lecture_id_seq.nextval, 4, 'Part 1', 'Mega kontent', 1, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'  ),
(lecture_id_seq.nextval, 4, 'Part 2', 'Mega kontent druhy', 2, '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');


