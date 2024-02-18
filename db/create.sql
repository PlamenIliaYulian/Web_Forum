create table avatars
(
    avatar_id int auto_increment
        primary key,
    avatar    varchar(500) not null
);

create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(50) not null
);

create table tags
(
    tag_id     int auto_increment
        primary key,
    name       varchar(30)          null,
    is_deleted tinyint(1) default 0 null
);

create table users
(
    user_id    int auto_increment
        primary key,
    user_email varchar(50)          null,
    password   varchar(50)          not null,
    first_name varchar(32)          not null,
    last_name  varchar(32)          not null,
    is_deleted tinyint(1) default 0 not null,
    is_blocked tinyint(1) default 0 not null,
    username   varchar(20)          not null
);

create table avatars_users
(
    avatar_id int not null,
    user_id   int not null,
    constraint avatars_users_avatars_avatar_id_fk
        foreign key (avatar_id) references avatars (avatar_id),
    constraint avatars_users_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table comments
(
    comment_id int auto_increment
        primary key,
    content    varchar(4096)                          not null,
    is_deleted tinyint(1) default 0                   null,
    user_id    int                                    not null,
    created_on timestamp  default current_timestamp() not null on update current_timestamp(),
    likes      int                                    not null,
    dislikes   int                                    not null,
    constraint comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table comments_users_dislikes
(
    comment_id int not null,
    user_id    int not null,
    constraint comments_users_dislikes_comments_comment_id_fk
        foreign key (comment_id) references comments (comment_id),
    constraint comments_users_dislikes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create definer = web_forum_admin@`%` trigger decrease_comment_dislikes
    after delete
    on comments_users_dislikes
    for each row
    UPDATE comments
    SET dislikes = dislikes - 1
    WHERE comment_id = OLD.comment_id;

create definer = web_forum_admin@`%` trigger increase_comment_dislikes
    after insert
    on comments_users_dislikes
    for each row
    UPDATE comments
    SET dislikes = dislikes + 1
    WHERE comment_id = NEW.comment_id;

create table comments_users_likes
(
    comment_id int not null,
    user_id    int not null,
    constraint comments_users_likes_comments_comment_id_fk
        foreign key (comment_id) references comments (comment_id),
    constraint comments_users_likes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create definer = web_forum_admin@`%` trigger decrease_comment_likes
    after delete
    on comments_users_likes
    for each row
    UPDATE comments
    SET likes = likes - 1
    WHERE comment_id = OLD.comment_id;

create definer = web_forum_admin@`%` trigger increase_comment_likes
    after insert
    on comments_users_likes
    for each row
    UPDATE comments
    SET likes = likes + 1
    WHERE comment_id = NEW.comment_id;

create table phone_numbers
(
    phone_number_id int auto_increment
        primary key,
    phone_number    varchar(50) not null,
    user_id         int         not null,
    constraint phone_numbers_pk
        unique (phone_number),
    constraint phone_numbers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table posts
(
    post_id    int auto_increment
        primary key,
    title      varchar(64)                            not null,
    content    varchar(8192)                          not null,
    user_id    int                                    not null,
    created_on timestamp  default current_timestamp() not null on update current_timestamp(),
    is_deleted tinyint(1) default 0                   null,
    likes      int                                    not null,
    dislikes   int                                    not null,
    constraint posts_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table posts_comments
(
    post_id    int not null,
    comment_id int not null,
    constraint posts_comments_comments_comment_id_fk
        foreign key (comment_id) references comments (comment_id),
    constraint posts_comments_posts_post_id_fk
        foreign key (post_id) references posts (post_id)
);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    constraint posts_tags___fk
        foreign key (tag_id) references tags (tag_id),
    constraint posts_tags_posts_post_id_fk
        foreign key (post_id) references posts (post_id)
);

create table posts_users_dislikes
(
    post_id int not null,
    user_id int not null,
    constraint posts_users_dislikes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint posts_users_dislikes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create definer = web_forum_admin@`%` trigger decrease_post_dislikes
    after delete
    on posts_users_dislikes
    for each row
    UPDATE posts
    SET dislikes = dislikes - 1
    WHERE post_id = OLD.post_id;

create definer = web_forum_admin@`%` trigger increase_post_dislikes
    after insert
    on posts_users_dislikes
    for each row
    UPDATE posts
    SET dislikes = dislikes + 1
    WHERE post_id = NEW.post_id;

create table posts_users_likes
(
    post_id int null,
    user_id int not null,
    constraint posts_users_likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint posts_users_likes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create definer = web_forum_admin@`%` trigger decrease_post_likes
    after delete
    on posts_users_likes
    for each row
    UPDATE posts
    SET likes = likes - 1
    WHERE post_id = OLD.post_id;

create definer = web_forum_admin@`%` trigger increase_post_likes
    after insert
    on posts_users_likes
    for each row
    UPDATE posts
    SET likes = likes + 1
    WHERE post_id = NEW.post_id;

create table users_roles
(
    user_id int not null,
    role_id int not null,
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references users (user_id)
);