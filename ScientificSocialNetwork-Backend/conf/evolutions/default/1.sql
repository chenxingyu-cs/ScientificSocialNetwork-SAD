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
  time_stamp                    varchar(255),
  user_id                       bigint,
  post_title                    varchar(255),
  post_content                  varchar(255),
  link                          varchar(255),
  constraint pk_forum_post primary key (post_id)
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

create table publication_comment (
  id                            bigint auto_increment not null,
  publication_id                bigint,
  user_id                       bigint,
  user_name                     varchar(255),
  timestamp                     bigint,
  content                       varchar(255),
  thumb                         integer,
  constraint pk_publication_comment primary key (id)
);

create table publication_reply (
  id                            bigint auto_increment not null,
  publication_comment_id        bigint,
  status                        tinyint(1) default 0,
  from_user_id                  bigint,
  to_user_id                    bigint,
  timestamp                     bigint,
  content                       varchar(255),
  constraint pk_publication_reply primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  mailing_address               varchar(255),
  phone_number                  varchar(255),
  research_fields               varchar(255),
  constraint pk_user primary key (id)
);

alter table author_publication add constraint fk_author_publication_author foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_author_publication_author on author_publication (author_id);

alter table author_publication add constraint fk_author_publication_publication foreign key (publication_id) references publication (id) on delete restrict on update restrict;
create index ix_author_publication_publication on author_publication (publication_id);

alter table publication_comment add constraint fk_publication_comment_publication_id foreign key (publication_id) references publication (id) on delete restrict on update restrict;
create index ix_publication_comment_publication_id on publication_comment (publication_id);

alter table publication_reply add constraint fk_publication_reply_publication_comment_id foreign key (publication_comment_id) references publication_comment (id) on delete restrict on update restrict;
create index ix_publication_reply_publication_comment_id on publication_reply (publication_comment_id);

alter table publication_reply add constraint fk_publication_reply_from_user_id foreign key (from_user_id) references user (id) on delete restrict on update restrict;
create index ix_publication_reply_from_user_id on publication_reply (from_user_id);

alter table publication_reply add constraint fk_publication_reply_to_user_id foreign key (to_user_id) references user (id) on delete restrict on update restrict;
create index ix_publication_reply_to_user_id on publication_reply (to_user_id);


# --- !Downs

alter table author_publication drop foreign key fk_author_publication_author;
drop index ix_author_publication_author on author_publication;

alter table author_publication drop foreign key fk_author_publication_publication;
drop index ix_author_publication_publication on author_publication;

alter table publication_comment drop foreign key fk_publication_comment_publication_id;
drop index ix_publication_comment_publication_id on publication_comment;

alter table publication_reply drop foreign key fk_publication_reply_publication_comment_id;
drop index ix_publication_reply_publication_comment_id on publication_reply;

alter table publication_reply drop foreign key fk_publication_reply_from_user_id;
drop index ix_publication_reply_from_user_id on publication_reply;

alter table publication_reply drop foreign key fk_publication_reply_to_user_id;
drop index ix_publication_reply_to_user_id on publication_reply;

drop table if exists author;

drop table if exists author_publication;

drop table if exists forum_post;

drop table if exists publication;

drop table if exists publication_comment;

drop table if exists publication_reply;

drop table if exists user;

