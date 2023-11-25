CREATE TABLE wp_post(
    slug varchar(255) not null constraint wp_post_pkey primary key,
    post text not null
);
