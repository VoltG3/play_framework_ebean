
# --- Sample dataset

# --- !Ups

insert into department (id,name,created_at,company_id) values ( 1,'unknown department 1',now(),1);
insert into department (id,name,created_at,company_id) values ( 2,'unknown department 2',now(),2);


# --- !Downs

delete from department;
