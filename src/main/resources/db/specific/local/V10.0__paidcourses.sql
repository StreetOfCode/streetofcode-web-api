CREATE TABLE course_product(
    product_id varchar(255) not null constraint course_product_pkey primary key,
    course_id bigint constraint fk_course_id_in_course_price references course
);

create sequence user_product_id_seq;

CREATE TABLE user_product(
    id bigint not null constraint user_product_pkey primary key,
    soc_user_firebase_id varchar(255) not null constraint fk_soc_user_firebase_id_in_user_product references soc_user,
    product_id varchar(255) not null constraint fk_product_id_in_user_product references course_product,
    price_id varchar(255) not null,
    bought_at timestamp with time zone not null,
    constraint uc_user_product unique (soc_user_firebase_id, product_id)
);