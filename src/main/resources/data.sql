-- THIS FILE IS APPLIED ONLY IN DEVELOPMENT ON H2 DB. AFTER THIS FILE MIGRATIONS FROM FLYWAY ARE APPLIED

--################################################## CREATE AUTHORS ##################################################
INSERT INTO public.author (id, courses_title, description, email, image_url, name, slug) VALUES (1, 'Jakubove kurzy', 'V roku 2018 som vyštudoval FEI STU. Odvtedy som pracoval full-time na backende (Java, Spring Boot) ako aj na Frontende (React). Baví ma robiť kurziť a pomáhať ľudom učiť sa programovať.', 'jakub@streetofcode.sk', 'http://streetofcode.sk/wp-content/uploads/2022/09/ja2edited-scaled.jpg', 'Jakub Jahič', 'jakub-jahic');

--################################################## CREATE DIFFICULTIES ##################################################
INSERT INTO public.difficulty (id, name, skill_level) VALUES (1, 'Začiatočník', 1);
INSERT INTO public.difficulty (id, name, skill_level) VALUES (2, 'Mierne pokročilý', 2);
INSERT INTO public.difficulty (id, name, skill_level) VALUES (3, 'Pokročilý', 3);

--################################################## INFORMATIKA 101 ##################################################
INSERT INTO public.course (id, created_at, icon_url, lectures_count, long_description, name, resources, short_description, slug, status, thumbnail_url, trailer_url, updated_at, author_id, difficulty_id) VALUES (1, '2022-09-15 06:31:02.917482', 'http://streetofcode.sk/wp-content/uploads/2022/04/python-logo.png', 49, 'TODO', 'Informatika 101', '[Úlohy](https://www.youtube.com/redirect?event=comments&redir_token=QUFFLUhqbTdwMjRUanFDbG5HR1RwT0NVUVhXeEVrNXRFQXxBQ3Jtc0trZHpyTDBtbGtsNURuRHB6QjJEOF85OXJKM2ZEc21TRzB1NUJNV3BmeFVVQXhZanZZWE5MX2sxTnVsQjZmTWp1X3hqV1BSVDU1VVk4TERqeUxoTGE2b0ZPVnI3akVsa0syejhhS2Y3YXJqVHk1MzB2Zw&q=http%3A%2F%2Fstreetofcode.sk%2Fwp-content%2Fuploads%2F2020%2F12%2FInformatika_101___Python_Programovanie.zip&stzid=UgwbA6juKylpuC8_YF54AaABAg.9HXBpDlFHIi9HXCdaPhQzv)', 'Úvod do informatiky a programovania v Pythone', 'informatika-101', 'DRAFT', '', '741434827', '2022-09-15 08:20:54.893829', 1, 1);

INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (2, 3, '2022-09-15 06:33:00.682759', 'Ako robí počítač rozhodnutia?', '2022-09-15 07:41:28.896916', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (1, 2, '2022-09-15 06:32:42.751078', 'Ako funguje počítač?', '2022-09-15 07:42:05.829912', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (3, 4, '2022-09-15 06:33:28.830092', 'Algoritmické myslenie', '2022-09-15 07:42:30.264287', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (4, 5, '2022-09-15 06:35:22.370627', 'Programovanie - Part 0', '2022-09-15 07:42:39.079399', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (5, 6, '2022-09-15 06:36:05.621276', 'Programovanie - Part 1', '2022-09-15 07:42:47.160239', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (6, 7, '2022-09-15 06:36:16.924437', 'Programovanie - Part 2', '2022-09-15 07:43:02.751880', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (7, 8, '2022-09-15 06:36:27.726498', 'Programovanie - Part 3', '2022-09-15 07:43:22.600172', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (8, 9, '2022-09-15 06:36:40.915475', 'Programovanie - Part 4', '2022-09-15 07:43:29.514282', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (9, 10, '2022-09-15 06:36:51.586238', 'Programovanie - Part 5', '2022-09-15 07:43:36.478511', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (10, 11, '2022-09-15 06:37:21.708821', 'Programovanie - Part 6', '2022-09-15 07:43:43.318068', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (11, 12, '2022-09-15 06:37:59.558048', 'Programovanie - Part 7', '2022-09-15 07:43:50.478642', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (12, 13, '2022-09-15 06:38:18.792784', 'Programovanie - Part 8', '2022-09-15 07:44:02.272831', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (13, 14, '2022-09-15 06:41:41.217752', 'Zadanie - zelovoc', '2022-09-15 07:44:09.415961', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (14, 15, '2022-09-15 06:42:04.210770', 'Čo ďalej?', '2022-09-15 07:44:23.043339', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (15, 16, '2022-09-15 06:42:24.109029', 'Bonus', '2022-09-15 07:44:33.847688', 1);
INSERT INTO public.chapter (id, chapter_order, created_at, name, updated_at, course_id) VALUES (16, 1, '2022-09-15 07:46:05.163563', 'Úvod do kurzu', '2022-09-15 07:46:05.163563', 1);

INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (1, null, '2022-09-15 07:39:02.366252', 1, 'Hardvér', '2022-09-15 07:39:02.366252', 403, '743763775', 1);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (2, null, '2022-09-15 07:39:28.994483', 2, 'Softvér', '2022-09-15 07:39:28.994483', 171, '743765204', 1);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (3, null, '2022-09-15 07:39:55.643405', 3, 'Binárna sústava', '2022-09-15 07:39:55.643405', 1209, '743761577', 1);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (4, null, '2022-09-15 07:40:22.867185', 4, 'Reprezentácia dát pomocou binárnej sústavy', '2022-09-15 07:40:22.867185', 930, '746269369', 1);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (5, null, '2022-09-15 07:40:41.968562', 5, 'Program a programovacie jazyky', '2022-09-15 07:40:41.968562', 469, '743764452', 1);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (6, null, '2022-09-15 07:47:16.738283', 1, 'O čom je tento kurz?', '2022-09-15 07:47:16.738283', 466, '743765525', 16);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (7, null, '2022-09-15 07:47:51.522265', 2, 'Prehľad kapitol', '2022-09-15 07:47:51.522265', 294, '743766221', 16);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (8, '[Stiahni si úlohy](https://www.youtube.com/redirect?event=comments&redir_token=QUFFLUhqazhHMWk5UVNwaU14VllrcVctdHQ0d3BNTDdld3xBQ3Jtc0tsMlYyOG9EMVNkVEhOcGNFWFNuZFpSRW44cDVvVVJIV1JVazdHV2c5U1Zsd0RjR1I1aW5DMFFGdjVOcDFCcVVJUHp3dHA5THdPVE43YUNtTnZFLUpCbEJaZzBsRHhnaFVnTHRYSUR4MUJ1bGYxb0Npaw&q=https%3A%2F%2Fdocs.google.com%2Fdocument%2Fd%2F1XxnzuyIWivtMgHwb2vaM5MfFHjr9QZ4ioTB4E1RiWzc%2Fedit%3Fusp%3Dsharing&stzid=Ugwojg41-WL8at0Hvd94AaABAg)', '2022-09-15 07:48:48.968626', 3, 'Ako si robiť úlohy?', '2022-09-15 07:48:48.968626', 499, '743908540', 16);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (9, null, '2022-09-15 07:49:26.558336', 4, 'Tipy ako pozerať kurz', '2022-09-15 07:49:26.558336', 118, '743766571', 16);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (11, null, '2022-09-15 07:52:54.525264', 1, 'Rozhodovací strom', '2022-09-15 07:52:54.525264', 525, '743759258', 2);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (12, null, '2022-09-15 07:56:00.404236', 1, 'Čo je to algoritmus?', '2022-09-15 07:56:00.404236', 715, '743759604', 3);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (13, null, '2022-09-15 07:56:18.142417', 2, 'Hádaj číslo', '2022-09-15 07:56:18.142417', 367, '743760375', 3);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (14, null, '2022-09-15 07:56:37.323476', 3, 'Programovanie je ťažké', '2022-09-15 07:56:37.323476', 444, '743760994', 3);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (15, null, '2022-09-15 07:57:58.419451', 1, 'Inštalácia Pythonu', '2022-09-15 07:57:58.419451', 355, '743915566', 4);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (16, null, '2022-09-15 07:58:24.115308', 2, 'Inštalácia PyCharm', '2022-09-15 07:58:24.115308', 421, '743917092', 4);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (17, null, '2022-09-15 07:58:54.372064', 1, 'Premenné', '2022-09-15 07:58:54.372064', 641, '743930791', 5);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (18, null, '2022-09-15 07:59:16.928865', 2, 'Dátové typy', '2022-09-15 07:59:16.928865', 407, '743919141', 5);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (19, null, '2022-09-15 07:59:40.428255', 3, 'Aritmetické operátory', '2022-09-15 07:59:40.428255', 380, '743928179', 5);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (20, null, '2022-09-15 08:00:07.724627', 4, 'Načítanie vstupu', '2022-09-15 08:00:07.724627', 1018, '743921046', 5);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (21, null, '2022-09-15 08:00:47.971297', 1, 'Zopakovanie a komenty', '2022-09-15 08:00:47.971297', 957, '743947442', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (22, null, '2022-09-15 08:01:09.238151', 2, 'Podmienené príkazy', '2022-09-15 08:01:09.238151', 851, '743941060', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (23, null, '2022-09-15 08:01:24.748011', 3, 'Výroková logika', '2022-09-15 08:01:24.748011', 383, '743945839', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (24, null, '2022-09-15 08:01:46.302302', 4, 'Logické operátory', '2022-09-15 08:01:46.302302', 538, '743937588', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (25, null, '2022-09-15 08:02:00.658606', 5, 'Debuggovanie', '2022-09-15 08:02:00.658606', 553, '743934694', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (26, null, '2022-09-15 08:03:09.649131', 6, 'Tipy a trocha motivácie 1', '2022-09-15 08:03:09.649131', 67, '743910317', 6);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (27, null, '2022-09-15 08:03:36.240529', 1, 'Funkcie', '2022-09-15 08:03:36.240529', 1204, '743958489', 7);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (28, null, '2022-09-15 08:03:52.247001', 2, 'Globálne a lokálne premenné', '2022-09-15 08:03:52.247001', 599, '743962661', 7);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (29, null, '2022-09-15 08:04:13.907993', 3, 'Riešime úlohy', '2022-09-15 08:04:13.907993', 781, '743964094', 7);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (30, null, '2022-09-15 08:05:03.064458', 1, 'For cyklus', '2022-09-15 08:05:03.064458', 858, '743966635', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (31, null, '2022-09-15 08:05:17.666377', 2, 'While cyklus', '2022-09-15 08:05:17.666377', 949, '743976384', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (32, null, '2022-09-15 08:05:36.370142', 3, 'Riešime ľahšie úlohy', '2022-09-15 08:05:36.370142', 1523, '743969122', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (33, null, '2022-09-15 08:05:57.624907', 4, 'Riešime ťažšie úlohy', '2022-09-15 08:05:57.624907', 1872, '743972437', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (34, null, '2022-09-15 08:06:22.386386', 5, 'Odchytávame chyby', '2022-09-15 08:06:22.386386', 252, '743975832', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (35, null, '2022-09-15 08:06:43.358008', 6, 'Tipy a trocha motivácie 2', '2022-09-15 08:06:43.358008', 277, '743910666', 8);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (36, null, '2022-09-15 08:07:23.009767', 1, 'Dokumentácia', '2022-09-15 08:07:23.009767', 414, '743977498', 9);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (37, null, '2022-09-15 08:07:45.337882', 2, 'Vlastné moduly', '2022-09-15 08:07:45.337882', 424, '743978128', 9);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (38, null, '2022-09-15 08:07:59.981597', 3, 'Zabudované moduly', '2022-09-15 08:07:59.981597', 572, '743978697', 9);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (39, null, '2022-09-15 08:08:31.499531', 1, 'Čítanie zo súboru', '2022-09-15 08:08:31.499531', 1361, '743979590', 10);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (40, null, '2022-09-15 08:08:47.410608', 2, 'Písanie do súboru', '2022-09-15 08:08:47.410608', 458, '743981512', 10);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (41, null, '2022-09-15 08:09:20.300018', 1, 'Listy 1', '2022-09-15 08:09:20.300018', 303, '743984701', 11);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (42, null, '2022-09-15 08:09:31.485696', 2, 'Listy 2', '2022-09-15 08:09:31.485696', 709, '743985224', 11);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (43, null, '2022-09-15 08:09:45.143770', 3, 'Listy 3', '2022-09-15 08:09:45.143770', 1042, '743986432', 11);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (44, null, '2022-09-15 08:10:04.786411', 4, 'Dictionary (Slovníky)', '2022-09-15 08:10:04.786411', 1505, '743982159', 11);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (45, null, '2022-09-15 08:10:18.674767', 5, 'Tuples', '2022-09-15 08:10:18.674767', 1074, '743988123', 11);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (46, null, '2022-09-15 08:10:45.040630', 1, 'Stringy', '2022-09-15 08:10:45.040630', 883, '746270993', 12);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (47, 'Spravme si malý program, ktorý nazveme **Zelovoc** (alebo akýkoľvek iný obchod). Na konci tohto článku môžeš nájsť malé hinty ako na to. Obchod by mal spĺňať nasledovné **požiadavky**:

- Produkt je iba obyčajný reťazec – slovo (napr. „jablko“).
- Cena produktu, je dĺžka jeho názvu (napr. „jablko“ u nás stojí 6 jednotiek, „paradajka“ stojí 9 jednotiek).
- Naše produkty potrebujeme niekde uložiť (napr. do listu).
- Naše produkty chceme nejakým spôsobom ukázať používateľovi. Vypísať pri nich chceme ich poradie, názov a cenu produktu. Poradie nech nám začína od 1, nie od 0.
- Po vypísaní produktov používateľovi chceme dať možnosť si nejaký produkt kúpiť. To môžeš spraviť tak, že si od neho vypýtaš číslo, ktoré bude označovať poradie produktu, ktorý si chce kúpiť.
- Keď si používateľ kúpi produkt, tak ho niekam treba uložiť (napr. do listu, ktorý sa bude volať shopping_cart).
- Po kúpe produktu chceme znova vypísať produkty a znova umožniť nákup (kedy program končí je popísané nižšie).
- Naše produkty máme iba po jednom kuse. To znamená, že keď si používateľ kúpi produkt, tak už mu ho nemôžeš ponúkať znovu.
- K vypisovaniu produktov pridaj aj vypísanie používateľovho košíka. Produkty v košíku stačí vypísať za sebou. Na koniec mu tam ešte napíš aktuálnu cenu košíka.
- Ak používateľ pri voľbe produktu zadá číslo 0, tak to znamená, že si nákup želá ukončiť. Túto možnosť mu napíš pod zoznam produktov, nech vie, ako má nákup ukončiť.
- Po ukončení nákupu mu napíš, aké produkty si kúpil a koľko za ne má zaplatiť.
- Na záver sa mu nezabudni aj poďakovať.

### Bonus 1
Ako bonus si môžeš kód trochu upraviť. Vo svete programátora nestačí, že kód funguje. Kód sa má dať aj dobre čítať a jednoducho upravovať:
- Sprav si funkciu na výpočet ceny produktu, nech nemusíš vkuse volať len()
- Sprav si funkciu na výpis všetkých produktov
- Sprav si funkciu na výpis košíka
- Sprav si funkciu na získanie produktu od používateľa
### Bonus 2
Vylepši tvoj obchod tak, aby vedel mať viacero kusov daného produktu. Zákazníkovi ponúkaj produkt až pokým je dostupný aspoň 1 kus
### Bonus 3
Podobným spôsobom akým sa v BONUSE 2 robí počet produktov, skús upraviť cenu produktov. To znamená, že cena už nebude dĺžka mena produktu, ale bude určená a niekde uložená.', '2022-09-15 08:15:37.273574', 1, 'Zelovoc', '2022-09-15 08:15:37.273574', 1910, '746272346', 13);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (48, null, '2022-09-15 08:16:20.649536', 1, 'Čo môžeš robiť ďalej?', '2022-09-15 08:16:20.649536', 529, '743912415', 14);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (49, null, '2022-09-15 08:16:43.527186', 2, 'Záverečný odkaz', '2022-09-15 08:16:43.527186', 87, '743915035', 14);
INSERT INTO public.lecture (id, content, created_at, lecture_order, name, updated_at, video_duration_seconds, video_url, chapter_id) VALUES (50, '[Video na YouTube s lepšou kvalitou](https://www.youtube.com/watch?v=hfbjBnnb_4Y&list=PL5Od02qMtU8j4Evu2H-eHSXuNSVlfrhcx&index=50)', '2022-09-15 08:17:40.264209', 1, 'Gabo rieši úlohy', '2022-09-15 08:17:40.264209', 8058, '749862993', 15);


--################################################## LOCAL RANDOM STUFF ##################################################
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
