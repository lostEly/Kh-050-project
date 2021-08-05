delete from orderr;
delete from doctor;
delete from patient;
delete from userr;
delete from procedure;


insert into userr (user_id, email, last_name, login, name, password, phone) values (1, 'test1@email.com', 'DoctorLastName1', 'login1', 'DoctorName1', 'pwd1', '1234567980');
insert into userr (user_id, email, last_name, login, name, password, phone) values (2, 'test2@email.com', 'DoctorLastName2', 'login2', 'DoctorName2', 'pwd2', '1234567981');
insert into userr (user_id, email, last_name, login, name, password, phone) values (3, 'test3@email.com', 'DoctorLastName3', 'login3', 'DoctorName3', 'pwd3', '1234567982');
insert into userr (user_id, email, last_name, login, name, password, phone) values (4, 'test4@email.com', 'DoctorLastName4', 'login4', 'DoctorName4', 'pwd4', '1234567983');

insert into doctor (certificate_number, specialization, doctor_id) values ('124345', 'spec1', 1);
insert into doctor (certificate_number, specialization, doctor_id) values ('124346', 'spec2', 2);
insert into patient (insurance_number, patient_id) values ('124345', 3);
insert into patient (insurance_number, patient_id) values ('124346', 4);

insert into procedure (procedure_id, name, cost, duration) values (1, 'Procedure1', 10, '15:00:00');
insert into procedure (procedure_id, name, cost, duration) values (2, 'Procedure2', 20, '10:00:00');
insert into procedure (procedure_id, name, cost, duration) values (3, 'Procedure3', 30, '15:00:00');
insert into procedure (procedure_id, name, cost, duration) values (4, 'Procedure4', 40, '10:00:00');

insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id, patient_id) values (1, '2021-08-04 10:10:00', '2021-08-04 10:20:00', 10.0, 1,1,3);
insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id, patient_id) values (2, '2021-08-05 15:10:00', '2021-08-05 15:20:00', 20.0, 2,1,4);
insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id, patient_id) values (3, '2021-08-06 10:10:00', '2021-08-06 10:20:00', 30.0, 1,2,3);
insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id            ) values (4, '2021-08-07 10:10:00', '2021-08-07 10:20:00', 40.0, 2,1  );
insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id            ) values (5, '2021-08-08 10:10:00', '2021-08-08 10:20:00', 50.0, 1,2  );
insert into orderr (order_id, start, finish, cost, procedure_id, doctor_id            ) values (6, '2021-08-09 10:10:00', '2021-08-09 10:20:00', 60.0, 2,2  );
insert into orderr (order_id, start, finish, cost, procedure_id                       ) values (7, '2021-08-10 10:10:00', '2021-08-10 10:20:00', 70.0, 1    );
insert into orderr (order_id, start, finish, cost, procedure_id                       ) values (8, '2021-08-11 10:10:00', '2021-08-11 10:20:00', 80.0, 2    );
