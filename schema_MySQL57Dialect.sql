alter table ACCOUNT drop foreign key FK9rxadd7hdlmarov6qbvaot0po;
alter table ACCOUNT drop foreign key FK4cfkq2sywnlcu6fxm9xbrmsjs;
alter table CITIES drop foreign key FKbe39gncbw7tmfjfamr00x4qjv;
alter table CITIES drop foreign key FKktfg6x187el29cnyjhhpvawca;
alter table STATIONS drop foreign key FKldqyeie4uufe44knj6i4x2csv;
alter table STATIONS drop foreign key FKqn5h21f3qh4noflqab0hmw7oc;
alter table STATIONS drop foreign key FKo539o7bif6yu9a1w38n8fcpmd;
drop table if exists ACCOUNT;
drop table if exists CITIES;
drop table if exists STATIONS;
create table ACCOUNT (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), CREATED_BY integer, MODIFIED_BY integer, primary key (ID)) engine=InnoDB;
create table CITIES (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), DISTRICT varchar(32), NAME varchar(32) not null, REGION varchar(32) not null, CREATED_BY integer, MODIFIED_BY integer, primary key (ID)) engine=InnoDB;
create table STATIONS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), APARTMENT varchar(16), HOUSE_NO varchar(16) not null, STREET varchar(32) not null, ZIP_CODE varchar(10) not null, X double precision, Y double precision, PHONE varchar(16), TRANSPORT_TYPE varchar(255) not null, CREATED_BY integer, MODIFIED_BY integer, CITY_ID integer not null, primary key (ID)) engine=InnoDB;
alter table CITIES add constraint UK_mjjeh6je35mtsmrdm92x6rd1a unique (REGION);
alter table ACCOUNT add constraint FK9rxadd7hdlmarov6qbvaot0po foreign key (CREATED_BY) references ACCOUNT (ID);
alter table ACCOUNT add constraint FK4cfkq2sywnlcu6fxm9xbrmsjs foreign key (MODIFIED_BY) references ACCOUNT (ID);
alter table CITIES add constraint FKbe39gncbw7tmfjfamr00x4qjv foreign key (CREATED_BY) references ACCOUNT (ID);
alter table CITIES add constraint FKktfg6x187el29cnyjhhpvawca foreign key (MODIFIED_BY) references ACCOUNT (ID);
alter table STATIONS add constraint FKldqyeie4uufe44knj6i4x2csv foreign key (CREATED_BY) references ACCOUNT (ID);
alter table STATIONS add constraint FKqn5h21f3qh4noflqab0hmw7oc foreign key (MODIFIED_BY) references ACCOUNT (ID);
alter table STATIONS add constraint FKo539o7bif6yu9a1w38n8fcpmd foreign key (CITY_ID) references CITIES (ID);
