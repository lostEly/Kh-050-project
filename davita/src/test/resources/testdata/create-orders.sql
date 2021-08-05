delete from orderr;
delete from doctor;
delete from userr;
delete from procedure;


insert into userr (user_id, email, last_name, login, name, password, phone) values (1, 'test1@email.com', 'DoctorLastName1', 'login1', 'DoctorName1', 'pwd1', '1234567980');
insert into userr (user_id, email, last_name, login, name, password, phone) values (2, 'test2@email.com', 'DoctorLastName2', 'login2', 'DoctorName2', 'pwd2', '1234567981');
insert into doctor (certificate_number, specialization, doctor_id) values ('124345', 'spec1', 1);
insert into doctor (certificate_number, specialization, doctor_id) values ('124346', 'spec2', 2);
insert into procedure (procedure_id, name, cost, duration) values (1, 'Procedure1', 10, '15:00:00');
insert into procedure (procedure_id, name, cost, duration) values (2, 'Procedure2', 20, '10:00:00');
insert into procedure (procedure_id, name, cost, duration) values (3, 'Procedure3', 30, '15:00:00');
insert into procedure (procedure_id, name, cost, duration) values (4, 'Procedure4', 40, '10:00:00');

insert into orderr (order_id, start, finish, cost, procedure_id) values (1, '2021-07-01 10:10:00', '2021-07-01 10:20:00', 10.0, 1);
insert into orderr (order_id, start, finish, cost, procedure_id) values (2, '2021-07-02 10:10:00', '2021-07-02 10:20:00', 20.0, 2);
insert into orderr (order_id, start, finish, cost, procedure_id) values (3, '2021-07-03 10:10:00', '2021-07-03 10:20:00', 30.0, 1);
insert into orderr (order_id, start, finish, cost, procedure_id) values (4, '2021-07-04 10:10:00', '2021-07-04 10:20:00', 40.0, 2);
insert into orderr (order_id, start, finish, cost, procedure_id) values (5, '2021-07-05 10:10:00', '2021-07-05 10:20:00', 50.0, 1);
insert into orderr (order_id, start, finish, cost, procedure_id) values (6, '2021-07-06 10:10:00', '2021-07-06 10:20:00', 60.0, 2);
insert into orderr (order_id, start, finish, cost, procedure_id) values (7, '2021-07-07 10:10:00', '2021-07-07 10:20:00', 70.0, 1);
insert into orderr (order_id, start, finish, cost, procedure_id) values (8, '2021-07-08 10:10:00', '2021-07-08 10:20:00', 80.0, 2);
