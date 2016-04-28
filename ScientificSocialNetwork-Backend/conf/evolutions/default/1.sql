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
  type                          varchar(255),
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

create table tag (
  id                            bigint auto_increment not null,
  tag_name                      varchar(255),
  constraint pk_tag primary key (id)
);

create table tag_publication (
  tag_id                        bigint not null,
  publication_id                bigint not null,
  constraint pk_tag_publication primary key (tag_id,publication_id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  access_level                  varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  mailing_address               varchar(255),
  phone_number                  varchar(255),
  research_fields               varchar(255),
  self_id                       bigint,
  constraint pk_user primary key (id)
);

create table user_user_group (
  user_id                       bigint not null,
  user_group_id                 bigint not null,
  constraint pk_user_user_group primary key (user_id,user_group_id)
);

create table user_group (
  id                            bigint auto_increment not null,
  creator_user                  bigint,
  group_name                    varchar(255),
  group_description             varchar(255),
  access                        integer,
  topic                         varchar(255),
  constraint pk_user_group primary key (id)
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

alter table publication_comment add constraint fk_publication_comment_publication_id foreign key (publication_id) references publication (id) on delete restrict on update restrict;
create index ix_publication_comment_publication_id on publication_comment (publication_id);

alter table publication_reply add constraint fk_publication_reply_publication_comment_id foreign key (publication_comment_id) references publication_comment (id) on delete restrict on update restrict;
create index ix_publication_reply_publication_comment_id on publication_reply (publication_comment_id);

alter table publication_reply add constraint fk_publication_reply_from_user_id foreign key (from_user_id) references user (id) on delete restrict on update restrict;
create index ix_publication_reply_from_user_id on publication_reply (from_user_id);

alter table publication_reply add constraint fk_publication_reply_to_user_id foreign key (to_user_id) references user (id) on delete restrict on update restrict;
create index ix_publication_reply_to_user_id on publication_reply (to_user_id);

alter table tag_publication add constraint fk_tag_publication_tag foreign key (tag_id) references tag (id) on delete restrict on update restrict;
create index ix_tag_publication_tag on tag_publication (tag_id);

alter table tag_publication add constraint fk_tag_publication_publication foreign key (publication_id) references publication (id) on delete restrict on update restrict;
create index ix_tag_publication_publication on tag_publication (publication_id);

alter table user add constraint fk_user_self_id foreign key (self_id) references user (id) on delete restrict on update restrict;
create index ix_user_self_id on user (self_id);

alter table user_user_group add constraint fk_user_user_group_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_user_group_user on user_user_group (user_id);

alter table user_user_group add constraint fk_user_user_group_user_group foreign key (user_group_id) references user_group (id) on delete restrict on update restrict;
create index ix_user_user_group_user_group on user_user_group (user_group_id);


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

alter table publication_comment drop foreign key fk_publication_comment_publication_id;
drop index ix_publication_comment_publication_id on publication_comment;

alter table publication_reply drop foreign key fk_publication_reply_publication_comment_id;
drop index ix_publication_reply_publication_comment_id on publication_reply;

alter table publication_reply drop foreign key fk_publication_reply_from_user_id;
drop index ix_publication_reply_from_user_id on publication_reply;

alter table publication_reply drop foreign key fk_publication_reply_to_user_id;
drop index ix_publication_reply_to_user_id on publication_reply;

alter table tag_publication drop foreign key fk_tag_publication_tag;
drop index ix_tag_publication_tag on tag_publication;

alter table tag_publication drop foreign key fk_tag_publication_publication;
drop index ix_tag_publication_publication on tag_publication;

alter table user drop foreign key fk_user_self_id;
drop index ix_user_self_id on user;

alter table user_user_group drop foreign key fk_user_user_group_user;
drop index ix_user_user_group_user on user_user_group;

alter table user_user_group drop foreign key fk_user_user_group_user_group;
drop index ix_user_user_group_user_group on user_user_group;

drop table if exists author;

drop table if exists author_publication;

drop table if exists forum_post;

drop table if exists forum_post_comment;

drop table if exists forum_post_comment_rating;

drop table if exists forum_post_rating;

drop table if exists publication;

drop table if exists publication_comment;

drop table if exists publication_reply;

drop table if exists tag;

drop table if exists tag_publication;

drop table if exists user;

drop table if exists user_user_group;

drop table if exists user_group;

