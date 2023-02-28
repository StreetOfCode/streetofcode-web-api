create sequence post_comment_id_seq;

CREATE TABLE post_comment(
    id bigint not null constraint post_comment_pkey primary key,
    soc_user_firebase_id varchar(255) constraint fk_soc_firebase_id_in_post references soc_user,
    post_id varchar(255) not null,
    post_title varchar(255) not null,
    comment_text text not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

CREATE INDEX post_id_index ON post_comment (post_id);
