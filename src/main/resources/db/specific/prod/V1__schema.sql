create sequence author_id_seq;

alter sequence author_id_seq owner to postgres;

create sequence chapter_id_seq;

alter sequence chapter_id_seq owner to postgres;

create sequence course_id_seq;

alter sequence course_id_seq owner to postgres;

create sequence course_review_id_seq;

alter sequence course_review_id_seq owner to postgres;

create sequence difficulty_id_seq;

alter sequence difficulty_id_seq owner to postgres;

create sequence lecture_comment_id_seq;

alter sequence lecture_comment_id_seq owner to postgres;

create sequence lecture_id_seq;

alter sequence lecture_id_seq owner to postgres;

create sequence next_course_vote_id_seq;

alter sequence next_course_vote_id_seq owner to postgres;

create sequence next_course_vote_option_id_seq;

alter sequence next_course_vote_option_id_seq owner to postgres;

create sequence progress_lecture_id_seq;

alter sequence progress_lecture_id_seq owner to postgres;

create sequence quiz_id_seq;

alter sequence quiz_id_seq owner to postgres;

create sequence quiz_question_answer_id_seq;

alter sequence quiz_question_answer_id_seq owner to postgres;

create sequence quiz_question_id_seq;

alter sequence quiz_question_id_seq owner to postgres;

create sequence quiz_question_user_answer_id_seq;

alter sequence quiz_question_user_answer_id_seq owner to postgres;

create sequence user_progress_metadata_id_seq;

alter sequence user_progress_metadata_id_seq owner to postgres;

create table if not exists author
(
    id            bigint       not null
        constraint author_pkey
            primary key,
    courses_title varchar(255) not null,
    description   varchar(255) not null,
    email         varchar(255) not null,
    image_url     varchar(255) not null,
    name          varchar(255) not null,
    slug          varchar(255) not null
        constraint uk_a247bd901toi15s4mucfsj4ur
            unique
);

alter table author
    owner to postgres;

create table if not exists difficulty
(
    id          bigint       not null
        constraint difficulty_pkey
            primary key,
    name        varchar(255) not null,
    skill_level integer      not null
);

alter table difficulty
    owner to postgres;

create table if not exists course
(
    id                bigint                   not null
        constraint course_pkey
            primary key,
    created_at        timestamp with time zone not null,
    icon_url          varchar(255)             not null,
    lectures_count    integer default 0        not null,
    long_description  text                     not null,
    name              varchar(255)             not null,
    resources         text,
    short_description varchar(255)             not null,
    slug              varchar(255)             not null
        constraint uk_dduji3l55k63yvfcxs7n07bba
            unique,
    status            varchar(255)             not null,
    thumbnail_url     varchar(255),
    trailer_url       varchar(255),
    updated_at        timestamp with time zone not null,
    author_id         bigint
        constraint fk5t0ynyp27x7h1rny57tvgjb3o
            references author,
    difficulty_id     bigint
        constraint fk6v56hib5bc6h9jcpvecnxi80r
            references difficulty
);

alter table course
    owner to postgres;

create table if not exists chapter
(
    id            bigint                   not null
        constraint chapter_pkey
            primary key,
    chapter_order integer                  not null,
    created_at    timestamp with time zone not null,
    name          varchar(255)             not null,
    updated_at    timestamp with time zone not null,
    course_id     bigint                   not null
        constraint fkhhaina8rg7bpmg1qesiluu8vu
            references course
);

alter table chapter
    owner to postgres;

create table if not exists lecture
(
    id                     bigint                   not null
        constraint lecture_pkey
            primary key,
    content                text,
    created_at             timestamp with time zone not null,
    lecture_order          integer                  not null,
    name                   varchar(255)             not null,
    updated_at             timestamp with time zone not null,
    video_duration_seconds integer default 0        not null,
    video_url              text,
    chapter_id             bigint                   not null
        constraint fkk912p0rsmfkhgk3pyeuaxckvm
            references chapter
);

alter table lecture
    owner to postgres;

create table if not exists next_course_vote
(
    id                    bigint not null
        constraint next_course_vote_pkey
            primary key,
    next_course_option_id bigint not null,
    user_id               varchar(255)
);

alter table next_course_vote
    owner to postgres;

create table if not exists next_course_vote_option
(
    id       bigint       not null
        constraint next_course_vote_option_pkey
            primary key,
    disabled boolean,
    name     varchar(255) not null
);

alter table next_course_vote_option
    owner to postgres;

create table if not exists progress_lecture
(
    id         bigint                   not null
        constraint progress_lecture_pkey
            primary key,
    created_at timestamp with time zone not null,
    lecture_id bigint                   not null,
    user_id    varchar(255)             not null
);

alter table progress_lecture
    owner to postgres;

create table if not exists quiz
(
    id               bigint                   not null
        constraint quiz_pkey
            primary key,
    created_at       timestamp with time zone not null,
    finished_message varchar(255),
    subtitle         varchar(255),
    title            varchar(255)             not null,
    lecture_id       bigint
        constraint fkprmvpanqu0085qts8i8qskdfj
            references lecture
);

alter table quiz
    owner to postgres;

create table if not exists quiz_question
(
    id             bigint       not null
        constraint quiz_question_pkey
            primary key,
    question_order integer      not null,
    text           varchar(255) not null,
    type           varchar(255) not null,
    quiz_id        bigint       not null
        constraint fkdtynvfjgh6e7fd8l0wk37nrpc
            references quiz
);

alter table quiz_question
    owner to postgres;

create table if not exists quiz_question_answer
(
    id               bigint       not null
        constraint quiz_question_answer_pkey
            primary key,
    is_correct       boolean      not null,
    text             varchar(255) not null,
    quiz_question_id bigint       not null
        constraint fkg9c3quymsg6s2ir2teodybtgt
            references quiz_question
);

alter table quiz_question_answer
    owner to postgres;

create table if not exists quiz_question_user_answer
(
    id          bigint                   not null
        constraint quiz_question_user_answer_pkey
            primary key,
    created_at  timestamp with time zone not null,
    try_count   integer                  not null,
    user_id     varchar(255)             not null,
    answer_id   bigint                   not null
        constraint fkqsm55pcpycqu7j38av93u13yu
            references quiz_question_answer,
    question_id bigint                   not null
        constraint fkeb7u08nt7u0cpwbb54t1g00vc
            references quiz_question
);

alter table quiz_question_user_answer
    owner to postgres;

create table if not exists soc_user
(
    firebase_id        varchar(255) not null
        constraint soc_user_pkey
            primary key,
    email              varchar(255) not null,
    image_url          varchar(255),
    name               varchar(255) not null,
    receive_newsletter boolean      not null
);

alter table soc_user
    owner to postgres;

create table if not exists course_review
(
    id                   bigint                   not null
        constraint course_review_pkey
            primary key,
    course_id            bigint                   not null,
    created_at           timestamp with time zone not null,
    rating               double precision         not null,
    text                 text,
    updated_at           timestamp with time zone not null,
    soc_user_firebase_id varchar(255)             not null
        constraint fkhnqsofu22xb5sc30i5sij8e58
            references soc_user
);

alter table course_review
    owner to postgres;

create table if not exists lecture_comment
(
    id                   bigint                   not null
        constraint lecture_comment_pkey
            primary key,
    comment_text         text                     not null,
    created_at           timestamp with time zone not null,
    updated_at           timestamp with time zone not null,
    lecture_id           bigint                   not null
        constraint fknyspicr4wiky666g6yejckkqe
            references lecture,
    soc_user_firebase_id varchar(255)             not null
        constraint fkhtmrb9thyv309ffaroylij4is
            references soc_user
);

alter table lecture_comment
    owner to postgres;

create table if not exists user_progress_metadata
(
    id              bigint                   not null
        constraint user_progress_metadata_pkey
            primary key,
    course_id       bigint                   not null,
    finished_at     timestamp with time zone,
    last_updated_at timestamp with time zone not null,
    lectures_viewed integer                  not null,
    started_at      timestamp with time zone not null,
    status          varchar(255)             not null,
    user_id         varchar(255)             not null
);

alter table user_progress_metadata
    owner to postgres;

create table if not exists flyway_schema_history
(
    installed_rank integer                 not null
        constraint flyway_schema_history_pk
            primary key,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null
);

alter table flyway_schema_history
    owner to postgres;

create index if not exists flyway_schema_history_s_idx
    on flyway_schema_history (success);

