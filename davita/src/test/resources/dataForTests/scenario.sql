DELETE from procedure;
DELETE from equipment;
DELETE from doctor;
DELETE from patient;
DELETE from userr;
DELETE from orderr;


insert into equipment values (1, 'Equipment1');
insert into equipment values (2, 'Equipment2');
insert into equipment values (3, 'Equipment3');
insert into equipment values (4, 'Equipment4');
insert into equipment values (5, 'Equipment5');


insert into procedure values (1, 30.5, '12:15:30', 'procedure1', 1);
insert into procedure values (2, 40.5, '13:15:30', 'procedure2', 2);
insert into procedure values (3, 50.5, '14:15:30', 'procedure3', 3);
insert into procedure values (4, 60.5, '15:15:30', 'procedure4', 4);
insert into procedure values (5, 70.5, '16:15:30', 'procedure5', 5);

insert into userr (user_id, email, last_name, login, name, password, phone)
values (1, 'test@email.com', 'test', 'test', 'test', 'pwd', '1234567980');
insert into doctor (certificate_number, specialization, doctor_id)
values ('124345', 'doctor', 1);

insert into userr (user_id, email, last_name, login, name, password, phone)
values (2, 'doctor@email.com', 'Smith', 'smitty', 'John', 'pwd1', '1234567981');
insert into doctor (certificate_number, specialization, doctor_id)
values ('224345', 'doctor', 2);


insert into userr (user_id, email, last_name, login, name, password, phone)
values (3, 'patient1@email.com', 'test1', 'test1', 'test1', 'pwd1', '1234567231');
insert into patient (insurance_number, patient_id)
values ('1243454', 3);


insert into userr (user_id, email, last_name, login, name, password, phone)
values (4, 'patient2@email.com', 'Prowse', 'alien', 'Mason', 'pwd2', '1234567232');
insert into patient (insurance_number, patient_id)
values ('2243454', 4);




/*insert into orderr (order_id, cost, finish, start,  doctor_id, patient_id,procedure_id) values (1,10.0,
        '2021-08-09 09:00:00.000000','2021-08-09 08:00:00.000000',null,3,1);
insert into orderr (order_id, cost, finish, start,  doctor_id, patient_id,procedure_id) values (2,20.0,
        '2021-08-09 09:00:00.000000','2021-08-09 08:00:00.000000',null,4,2);*/