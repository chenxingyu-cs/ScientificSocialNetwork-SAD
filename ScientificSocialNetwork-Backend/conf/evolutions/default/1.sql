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


# --- !Downs

alter table author_publication drop foreign key fk_author_publication_author;
drop index ix_author_publication_author on author_publication;

alter table author_publication drop foreign key fk_author_publication_publication;
drop index ix_author_publication_publication on author_publication;

drop table if exists author;

drop table if exists author_publication;

drop table if exists publication;

drop table if exists user;

