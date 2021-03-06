alter table CITIES drop foreign key FKic0yipqoyj15kgdfw4yg30agw;
alter table CITIES drop foreign key FKsoag5c17rlm2taywgo5s4j4cj;
alter table ORDERS drop foreign key FKjyf1v3rrbg99txwdoh54hn6xk;
alter table ORDERS drop foreign key FK4yqyn2gaobau3191uge03ruhd;
alter table ORDERS drop foreign key FKstqs7160nvg92620732g0or5l;
alter table ORDERS drop foreign key FK6unyfh3sdm7gpq7qhpm4icgjp;
alter table ROUTES drop foreign key FKhyf4h57le1w6ewbsu84ng95mo;
alter table ROUTES drop foreign key FKenaxlx0yk2vxiuo0q1m617miv;
alter table ROUTES drop foreign key FKo49y0pmwdmi09a7d2r4gmwdsa;
alter table ROUTES drop foreign key FK5tusf897xxxf55p94idj9m0do;
alter table STATIONS drop foreign key FKrq8w179unt390xxu5480q3o5g;
alter table STATIONS drop foreign key FKdybictqbehfaahxsgpk2g8l7f;
alter table STATIONS drop foreign key FKo539o7bif6yu9a1w38n8fcpmd;
alter table TICKETS drop foreign key FKk8ccem5or0hn0gqn3yi1lwwpj;
alter table TICKETS drop foreign key FK1k638len1lxkiuun465t8dgla;
alter table TICKETS drop foreign key FKt9l075eyddis7gkvxv51dcsiu;
alter table TRIPS drop foreign key FKpoes1mks38o1npy91im1qyf25;
alter table TRIPS drop foreign key FKo2rbtcs1afhk3y287pua7taua;
alter table TRIPS drop foreign key FKb3tdfu93ai8f0je0m6rojf9mm;
alter table USERS drop foreign key FKogvei204bkwyfe2enow9mftjy;
alter table USERS drop foreign key FK3gblpwpmm7u56yo0fff8shblc;
drop table if exists CITIES;
drop table if exists ORDERS;
drop table if exists ROUTES;
drop table if exists STATIONS;
drop table if exists TICKETS;
drop table if exists TRIPS;
drop table if exists USERS;
create table CITIES (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), DISTRICT varchar(32), NAME varchar(32) not null, REGION varchar(32) not null, CREATED_BY integer, MODIFIED_BY integer, primary key (ID)) engine=InnoDB;
create table ORDERS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), CANCELLATION_REASON varchar(128), CLIENT_NAME varchar(32) not null, CLIENT_PHONE varchar(24) not null, DUE_DATE datetime(6) not null, ORDER_STATE varchar(255) not null, CREATED_BY integer, MODIFIED_BY integer, TICKET_ID integer not null, TRIP_ID integer not null, primary key (ID)) engine=InnoDB;
create table ROUTES (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), END_TIME time not null, PRICE double precision not null, START_TIME time not null, CREATED_BY integer, MODIFIED_BY integer, DESTINATION_ID integer not null, START_ID integer not null, primary key (ID)) engine=InnoDB;
create table STATIONS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), APARTMENT varchar(16), HOUSE_NO varchar(16) not null, STREET varchar(32) not null, ZIP_CODE varchar(10) not null, X double precision, Y double precision, PHONE varchar(16), TRANSPORT_TYPE varchar(255) not null, CREATED_BY integer, MODIFIED_BY integer, CITY_ID integer not null, primary key (ID)) engine=InnoDB;
create table TICKETS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), name varchar(32) not null, uid varchar(60) not null, CREATED_BY integer not null, MODIFIED_BY integer, TRIP_ID integer not null, primary key (ID)) engine=InnoDB;
create table TRIPS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), AVAILABLE_SEATS integer not null, END_TIME datetime(6) not null, MAX_SEATS integer not null, PRICE double precision not null, START_TIME datetime(6) not null, CREATED_BY integer, MODIFIED_BY integer, ROUTE_ID integer not null, primary key (ID)) engine=InnoDB;
create table USERS (ID integer not null auto_increment, CREATED_AT datetime(6) not null, MODIFIED_AT datetime(6), PASSWORD varchar(256) not null, USERNAME varchar(24) not null, CREATED_BY integer, MODIFIED_BY integer, primary key (ID)) engine=InnoDB;
alter table CITIES add constraint cityNameAndRegionUniqueConstraint unique (NAME, REGION);
alter table TICKETS add constraint UK_leh0178902t4y3ukbihmtv6pr unique (uid);
alter table USERS add constraint UK_h6k33r31i2nvrri9lok4r163j unique (USERNAME);
alter table CITIES add constraint FKic0yipqoyj15kgdfw4yg30agw foreign key (CREATED_BY) references USERS (ID);
alter table CITIES add constraint FKsoag5c17rlm2taywgo5s4j4cj foreign key (MODIFIED_BY) references USERS (ID);
alter table ORDERS add constraint FKjyf1v3rrbg99txwdoh54hn6xk foreign key (CREATED_BY) references USERS (ID);
alter table ORDERS add constraint FK4yqyn2gaobau3191uge03ruhd foreign key (MODIFIED_BY) references USERS (ID);
alter table ORDERS add constraint FKstqs7160nvg92620732g0or5l foreign key (TICKET_ID) references TICKETS (ID);
alter table ORDERS add constraint FK6unyfh3sdm7gpq7qhpm4icgjp foreign key (TRIP_ID) references TRIPS (ID);
alter table ROUTES add constraint FKhyf4h57le1w6ewbsu84ng95mo foreign key (CREATED_BY) references USERS (ID);
alter table ROUTES add constraint FKenaxlx0yk2vxiuo0q1m617miv foreign key (MODIFIED_BY) references USERS (ID);
alter table ROUTES add constraint FKo49y0pmwdmi09a7d2r4gmwdsa foreign key (DESTINATION_ID) references STATIONS (ID);
alter table ROUTES add constraint FK5tusf897xxxf55p94idj9m0do foreign key (START_ID) references STATIONS (ID);
alter table STATIONS add constraint FKrq8w179unt390xxu5480q3o5g foreign key (CREATED_BY) references USERS (ID);
alter table STATIONS add constraint FKdybictqbehfaahxsgpk2g8l7f foreign key (MODIFIED_BY) references USERS (ID);
alter table STATIONS add constraint FKo539o7bif6yu9a1w38n8fcpmd foreign key (CITY_ID) references CITIES (ID);
alter table TICKETS add constraint FKk8ccem5or0hn0gqn3yi1lwwpj foreign key (CREATED_BY) references USERS (ID);
alter table TICKETS add constraint FK1k638len1lxkiuun465t8dgla foreign key (MODIFIED_BY) references USERS (ID);
alter table TICKETS add constraint FKt9l075eyddis7gkvxv51dcsiu foreign key (TRIP_ID) references TRIPS (ID);
alter table TRIPS add constraint FKpoes1mks38o1npy91im1qyf25 foreign key (CREATED_BY) references USERS (ID);
alter table TRIPS add constraint FKo2rbtcs1afhk3y287pua7taua foreign key (MODIFIED_BY) references USERS (ID);
alter table TRIPS add constraint FKb3tdfu93ai8f0je0m6rojf9mm foreign key (ROUTE_ID) references ROUTES (ID);
alter table USERS add constraint FKogvei204bkwyfe2enow9mftjy foreign key (CREATED_BY) references USERS (ID);
alter table USERS add constraint FK3gblpwpmm7u56yo0fff8shblc foreign key (MODIFIED_BY) references USERS (ID);
