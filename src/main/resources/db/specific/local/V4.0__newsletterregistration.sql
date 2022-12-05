create sequence newsletter_registration_id_seq;

CREATE TABLE newsletter_registration(
    id bigint not null constraint newsletter_registration_pkey primary key,
    soc_user_firebase_id varchar(255),
    subscribed_from varchar(255) not null,
    created_at timestamp with time zone not null
);
