# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table publication (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  pages                         varchar(255),
  year                          integer,
  date                          varchar(255),
  url                           varchar(255),
  constraint pk_publication primary key (id)
);

create table task (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  done                          tinyint(1) default 0,
  due_date                      datetime(6),
  constraint pk_task primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists publication;

drop table if exists task;

drop table if exists user;

