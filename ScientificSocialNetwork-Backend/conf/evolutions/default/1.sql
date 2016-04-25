# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table author (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_author primary key (id)
);

create table author_publication (
  author_id                     bigint not null,
  publication_id                bigint not null,
  constraint pk_author_publication primary key (author_id,publication_id)
);

create table forum_post (
  post_id                       bigint auto_increment not null,
  user_id                       bigint not null,
  timestamp                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  post_title                    varchar(255),
  post_content                  TEXT,
  paper_link                    varchar(255),
  best_comment_id               bigint,
  constraint pk_forum_post primary key (post_id)
);

create table forum_post_comment (
  cid                           bigint auto_increment not null,
  post_id                       bigint,
  user_id                       bigint,
  timestamp                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  content                       TEXT,
  reply_to_user_id              bigint,
  vote                          bigint,
  constraint pk_forum_post_comment primary key (cid)
);

create table forum_post_comment_rating (
  rid                           bigint auto_increment not null,
  comment_id                    bigint,
  user_id                       bigint,
  updown                        integer,
  constraint pk_forum_post_comment_rating primary key (rid)
);

create table forum_post_rating (
  rid                           bigint auto_increment not null,
  post_id                       bigint,
  user_id                       bigint,
  updown                        integer,
  constraint pk_forum_post_rating primary key (rid)
);

create table publication (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  pages                         varchar(255),
  year                          integer,
  date                          varchar(255),
  url                           varchar(255),
  conference_name               varchar(255),
  count                         int default 0,
  constraint pk_publication primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  constraint pk_user primary key (id)
);

alter table author_publication add constraint fk_author_publication_author foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_author_publication_author on author_publication (author_id);

alter table author_publication add constraint fk_author_publication_publication foreign key (publication_id) references publication (id) on delete restrict on update restrict;
create index ix_author_publication_publication on author_publication (publication_id);

alter table forum_post add constraint fk_forum_post_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_forum_post_user_id on forum_post (user_id);

alter table forum_post_comment add constraint fk_forum_post_comment_post_id foreign key (post_id) references forum_post (post_id) on delete restrict on update restrict;
create index ix_forum_post_comment_post_id on forum_post_comment (post_id);

alter table forum_post_comment add constraint fk_forum_post_comment_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_forum_post_comment_user_id on forum_post_comment (user_id);

alter table forum_post_comment add constraint fk_forum_post_comment_reply_to_user_id foreign key (reply_to_user_id) references user (id) on delete restrict on update restrict;
create index ix_forum_post_comment_reply_to_user_id on forum_post_comment (reply_to_user_id);

alter table forum_post_comment_rating add constraint fk_forum_post_comment_rating_comment_id foreign key (comment_id) references forum_post_comment (cid) on delete restrict on update restrict;
create index ix_forum_post_comment_rating_comment_id on forum_post_comment_rating (comment_id);

alter table forum_post_comment_rating add constraint fk_forum_post_comment_rating_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_forum_post_comment_rating_user_id on forum_post_comment_rating (user_id);

alter table forum_post_rating add constraint fk_forum_post_rating_post_id foreign key (post_id) references forum_post (post_id) on delete restrict on update restrict;
create index ix_forum_post_rating_post_id on forum_post_rating (post_id);

alter table forum_post_rating add constraint fk_forum_post_rating_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_forum_post_rating_user_id on forum_post_rating (user_id);


# --- !Downs

alter table author_publication drop foreign key fk_author_publication_author;
drop index ix_author_publication_author on author_publication;

alter table author_publication drop foreign key fk_author_publication_publication;
drop index ix_author_publication_publication on author_publication;

alter table forum_post drop foreign key fk_forum_post_user_id;
drop index ix_forum_post_user_id on forum_post;

alter table forum_post_comment drop foreign key fk_forum_post_comment_post_id;
drop index ix_forum_post_comment_post_id on forum_post_comment;

alter table forum_post_comment drop foreign key fk_forum_post_comment_user_id;
drop index ix_forum_post_comment_user_id on forum_post_comment;

alter table forum_post_comment drop foreign key fk_forum_post_comment_reply_to_user_id;
drop index ix_forum_post_comment_reply_to_user_id on forum_post_comment;

alter table forum_post_comment_rating drop foreign key fk_forum_post_comment_rating_comment_id;
drop index ix_forum_post_comment_rating_comment_id on forum_post_comment_rating;

alter table forum_post_comment_rating drop foreign key fk_forum_post_comment_rating_user_id;
drop index ix_forum_post_comment_rating_user_id on forum_post_comment_rating;

alter table forum_post_rating drop foreign key fk_forum_post_rating_post_id;
drop index ix_forum_post_rating_post_id on forum_post_rating;

alter table forum_post_rating drop foreign key fk_forum_post_rating_user_id;
drop index ix_forum_post_rating_user_id on forum_post_rating;

drop table if exists author;

drop table if exists author_publication;

drop table if exists forum_post;

drop table if exists forum_post_comment;

drop table if exists forum_post_comment_rating;

drop table if exists forum_post_rating;

drop table if exists publication;

drop table if exists user;

