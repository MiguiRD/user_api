# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table telefono (
  id                            bigint not null,
  numero                        varchar(255),
  movil                         boolean,
  usuario_id                    bigint,
  constraint pk_telefono primary key (id)
);
create sequence telefono_seq;

create table usuario (
  id                            bigint not null,
  nombre                        varchar(255),
  apellidos                     varchar(255),
  constraint pk_usuario primary key (id)
);
create sequence usuario_seq;

alter table telefono add constraint fk_telefono_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_telefono_usuario_id on telefono (usuario_id);


# --- !Downs

alter table telefono drop constraint if exists fk_telefono_usuario_id;
drop index if exists ix_telefono_usuario_id;

drop table if exists telefono;
drop sequence if exists telefono_seq;

drop table if exists usuario;
drop sequence if exists usuario_seq;

