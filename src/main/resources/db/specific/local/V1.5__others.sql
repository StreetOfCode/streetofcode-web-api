INSERT INTO soc_user(firebase_id, name, email, image_url, receive_newsletter) VALUES
('moNoTwZcU5Nwg4qMBBVW9uJBQM12', 'Gabriel Kerekeš', 'gabriel@streetofcode.sk', 'https://streetofcode.sk/wp-content/uploads/2020/04/7520735.png', true),
('Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 'Jakub Jahič', 'jakub@streetofcode.sk', 'https://streetofcode.sk/wp-content/uploads/2019/04/JFinal-768x576.jpg', false);

INSERT INTO course_review (id, soc_user_firebase_id, course_id, rating, text, created_at, updated_at) VALUES
(course_review_id_seq.nextval, 'moNoTwZcU5Nwg4qMBBVW9uJBQM12', 1, 5, 'Mega kurz', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00'),
(course_review_id_seq.nextval, 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 1, 0, 'Na nic kurz', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00');

INSERT INTO lecture_comment(id, soc_user_firebase_id, lecture_id, comment_text, created_at, updated_at) VALUES
(lecture_comment_id_seq.nextval, 'moNoTwZcU5Nwg4qMBBVW9uJBQM12', 1, 'toto je super lekcia', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' ),
(lecture_comment_id_seq.nextval, 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', 1, 'parada', '2007-12-03T10:15:30+01:00', '2007-12-03T10:15:30+01:00' );

INSERT INTO quiz(id, lecture_id, title, subtitle, created_at, finished_message) VALUES
(quiz_id_seq.nextval, 1, 'Kvizik', 'Tu sa naucis matiku', '2007-12-03T10:15:30+01:00', 'Spravne!!!');

INSERT INTO quiz_question(id, quiz_id, question_order, text, type) VALUES
(quiz_question_id_seq.nextval, 1, 1, '2+2?', 'SINGLE_CHOICE'),
(quiz_question_id_seq.nextval, 1, 2, '2*2', 'SINGLE_CHOICE'),
(quiz_question_id_seq.nextval, 1, 3, '8-4', 'MULTIPLE_CHOICE');

INSERT INTO quiz_question_answer(id, quiz_question_id, text, is_correct) VALUES
(quiz_question_answer_id_seq.nextval, 1, '1', false),
(quiz_question_answer_id_seq.nextval, 1, '2', false),
(quiz_question_answer_id_seq.nextval, 1, '3', false),
(quiz_question_answer_id_seq.nextval, 1, '4', true),
(quiz_question_answer_id_seq.nextval, 2, '1', false),
(quiz_question_answer_id_seq.nextval, 2, '2', false),
(quiz_question_answer_id_seq.nextval, 2, '3', false),
(quiz_question_answer_id_seq.nextval, 2, '4', true),
(quiz_question_answer_id_seq.nextval, 3, '1', false),
(quiz_question_answer_id_seq.nextval, 3, '2', false),
(quiz_question_answer_id_seq.nextval, 3, '+4', true),
(quiz_question_answer_id_seq.nextval, 3, '4', true);

INSERT INTO quiz_question_user_answer(id, question_id, answer_id, user_id, created_at, try_count) VALUES
(quiz_question_user_answer_id_seq.nextval, 1, 2, 'Dk71hPkR9Fc6SJma3S1NvGcrkHe2', '2007-12-03T10:15:30+01:00', 3);

INSERT INTO next_course_vote_option(id, name) VALUES
(next_course_vote_option_id_seq.nextval, 'Python'),
(next_course_vote_option_id_seq.nextval, 'Java'),
(next_course_vote_option_id_seq.nextval, 'Kotlin'),
(next_course_vote_option_id_seq.nextval, 'OOP'),
(next_course_vote_option_id_seq.nextval, 'Git'),
(next_course_vote_option_id_seq.nextval, 'C#'),
(next_course_vote_option_id_seq.nextval, 'SQL');
