INSERT INTO soc_user(firebase_id, name, email, image_url, receive_newsletter) VALUES
('moNoTwZcU5Nwg4qMBBVW9uJBQM12', 'Gabriel Kerekeš', 'gabriel@streetofcode.sk', 'https://wp.streetofcode.sk/wp-content/uploads/2020/04/7520735.png', true),
('Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 'Jakub Jahič', 'jakub@streetofcode.sk', 'https://wp.streetofcode.sk/wp-content/uploads/2019/04/JFinal-768x576.jpg', false);

INSERT INTO course_review (id, soc_user_firebase_id, course_id, rating, text, created_at, updated_at) VALUES
(nextval('course_review_id_seq'), 'moNoTwZcU5Nwg4qMBBVW9uJBQM12', 1, 5, 'Mega kurz', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'),
(nextval('course_review_id_seq'), 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 1, 0, 'Na nic kurz', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

INSERT INTO lecture_comment(id, soc_user_firebase_id, lecture_id, comment_text, created_at, updated_at) VALUES
(nextval('lecture_comment_id_seq'), 'moNoTwZcU5Nwg4qMBBVW9uJBQM12', 1, 'toto je super lekcia', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(nextval('lecture_comment_id_seq'), 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 1, 'parada', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' );

INSERT INTO quiz(id, lecture_id, title, subtitle, created_at, finished_message) VALUES
(nextval('quiz_id_seq'), 1, 'Kvizik', 'Tu sa naucis matiku', '2007-12-03T10:15:30+01:00', 'Spravne!!!');

INSERT INTO quiz_question(id, quiz_id, question_order, text, type) VALUES
(nextval('quiz_question_id_seq'), 1, 1, '2+2?', 'SINGLE_CHOICE'),
(nextval('quiz_question_id_seq'), 1, 2, '2*2', 'SINGLE_CHOICE'),
(nextval('quiz_question_id_seq'), 1, 3, '8-4', 'MULTIPLE_CHOICE');

INSERT INTO quiz_question_answer(id, quiz_question_id, text, is_correct) VALUES
(nextval('quiz_question_answer_id_seq'), 1, '1', false),
(nextval('quiz_question_answer_id_seq'), 1, '2', false),
(nextval('quiz_question_answer_id_seq'), 1, '3', false),
(nextval('quiz_question_answer_id_seq'), 1, '4', true),
(nextval('quiz_question_answer_id_seq'), 2, '1', false),
(nextval('quiz_question_answer_id_seq'), 2, '2', false),
(nextval('quiz_question_answer_id_seq'), 2, '3', false),
(nextval('quiz_question_answer_id_seq'), 2, '4', true),
(nextval('quiz_question_answer_id_seq'), 3, '1', false),
(nextval('quiz_question_answer_id_seq'), 3, '2', false),
(nextval('quiz_question_answer_id_seq'), 3, '+4', true),
(nextval('quiz_question_answer_id_seq'), 3, '4', true);

INSERT INTO quiz(id, lecture_id, title, subtitle, created_at, finished_message) VALUES
(nextval('quiz_id_seq'), 2, 'Kvizik', 'Tu sa naucis matiku', '2007-12-03T10:15:30+01:00', 'Spravne!!!');

INSERT INTO quiz_question(id, quiz_id, question_order, text, type) VALUES
(nextval('quiz_question_id_seq'), 2, 1, '2+2?', 'SINGLE_CHOICE'),
(nextval('quiz_question_id_seq'), 2, 2, '2*2', 'SINGLE_CHOICE'),
(nextval('quiz_question_id_seq'), 2, 3, '8-4', 'MULTIPLE_CHOICE');

INSERT INTO quiz_question_answer(id, quiz_question_id, text, is_correct) VALUES
(nextval('quiz_question_answer_id_seq'), 4, '1', false),
(nextval('quiz_question_answer_id_seq'), 4, '2', false),
(nextval('quiz_question_answer_id_seq'), 4, '3', false),
(nextval('quiz_question_answer_id_seq'), 4, '4', true),
(nextval('quiz_question_answer_id_seq'), 5, '1', false),
(nextval('quiz_question_answer_id_seq'), 5, '2', false),
(nextval('quiz_question_answer_id_seq'), 5, '3', false),
(nextval('quiz_question_answer_id_seq'), 5, '4', true),
(nextval('quiz_question_answer_id_seq'), 6, '1', false),
(nextval('quiz_question_answer_id_seq'), 6, '2', false),
(nextval('quiz_question_answer_id_seq'), 6, '+4', true),
(nextval('quiz_question_answer_id_seq'), 6, '4', true);

INSERT INTO quiz_question_user_answer(id, question_id, answer_id, user_id, created_at, try_count) VALUES
(nextval('quiz_question_user_answer_id_seq'), 1, 2, 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', '2007-12-03T10:15:30+01:00', 3);
