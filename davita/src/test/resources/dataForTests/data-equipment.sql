DROP table equipment CASCADE;
DROP table procedure CASCADE;



create table equipment
(
    equipment_id int8 generated by default as identity,
    name         varchar(255) not null,
    primary key (equipment_id)
);

create table procedure
(
    procedure_id int8 generated by default as identity,
    cost         float8      not null,
    duration     time        not null,
    name         varchar(45) not null,
    equipment_id int8,
    primary key (procedure_id)
);



alter table if exists procedure
    add constraint FK8v2bb2llv23ix8bgpfitelaet
    foreign key (equipment_id)
    references equipment;


insert into equipment values (1, 'Equipment');
insert into equipment values (2, 'Equipment');
insert into equipment values (3, 'Equipment');
