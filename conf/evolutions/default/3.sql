
# --- First database schema

# --- !Ups

create table department (
  id                        bigint not null,
  name                      varchar(255),
  created_at                timestamp,
  company_id                bigint,
  constraint pk_department primary key (id))
;

create sequence department_seq start with 1000;

alter table department add constraint fk_department_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_department_company_1 on department (company_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table is exists department;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists department_seq;
