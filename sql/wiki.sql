create table category
(
    id     bigint           not null comment 'id'
        primary key,
    parent bigint default 0 not null comment 'id',
    name   varchar(50)      not null,
    sort   int              null
)
    collate = utf8mb4_general_ci;

create table content
(
    id      bigint     not null comment 'id'
        primary key,
    content mediumtext not null
)
    collate = utf8mb4_general_ci;

create table content_snapshot
(
    id         bigint     not null comment 'Id'
        primary key,
    content    mediumtext not null,
    date       datetime   not null,
    content_id bigint     not null
)
    collate = utf8mb4_general_ci;

create table doc
(
    id         bigint           not null comment 'id'
        primary key,
    ebook_id   bigint default 0 not null comment 'id',
    parent     bigint default 0 not null comment 'id',
    name       varchar(50)      null,
    sort       int              null,
    view_count int    default 0 null,
    vote_count int    default 0 null
)
    collate = utf8mb4_general_ci;

create table ebook
(
    id           bigint       not null comment 'id'
        primary key,
    name         varchar(50)  null,
    category1_id bigint       null comment '1',
    category2_id bigint       null comment '2',
    description  varchar(200) null,
    cover        varchar(200) null,
    doc_count    int          null,
    view_count   int          null,
    vote_count   int          null
)
    collate = utf8mb4_general_ci;

create table ebook_snapshot
(
    id            bigint auto_increment comment 'id'
        primary key,
    ebook_id      bigint default 0 not null comment 'id',
    date          date             not null,
    view_count    int    default 0 not null,
    vote_count    int    default 0 not null,
    view_increase int    default 0 not null,
    vote_increase int    default 0 not null,
    constraint ebook_id_date_unique
        unique (ebook_id, date)
)
    collate = utf8mb4_general_ci;

create table user
(
    id         bigint            not null comment 'ID'
        primary key,
    login_name varchar(50)       not null,
    name       varchar(50)       null,
    password   char(100)         not null,
    user_type  tinyint default 1 not null comment ' 0: ; 1: ',
    constraint login_name_unique
        unique (login_name)
)
    collate = utf8mb4_general_ci;

create table collect_doc
(
    id      bigint not null comment 'id'
        primary key,
    doc_id  bigint not null comment 'doc id',
    user_id bigint not null comment 'id',
    constraint collect_doc_FK
        foreign key (doc_id) references doc (id),
    constraint collect_doc_FK_1
        foreign key (user_id) references user (id)
);

