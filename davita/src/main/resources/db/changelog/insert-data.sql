insert into role (name) values ('ADMIN'), ('USER'), ('DOCTOR'), ('PATIENT');

insert into userr (date_of_birthday, email, last_name, login, name, password, phone) VALUES
('01/01/1970', 'doctor@gmail.com', 'Petrov', 'docPetrov', 'Petr', 'docpass', '0957894532'),
('15/01/1975', 'admin@gmail.com', 'Ivanov', 'adminIvanov', 'Ivan', 'adminpass', '0957897732'),
('02/02/1980', 'patient@gmail.com', 'Pupkin', 'patPupkin', 'Vasiliy', 'patpass', '0957885232');

insert into user_role (user_id, role_id) VALUES (1,2), (2,1), (3,2);

insert into doctor (certificate_number, specialization, doctor_id) VALUES
(754841321813, 'surgeon', 1);

insert into patient (insurance_number, patient_id) VALUES
(12151543215, 3);

insert into equipment (name) values ('inhaler'), ('x-ray');

insert into procedure (cost, duration, name, equipment_id) VALUES
(50, '01/01/1970 00:15:00', 'inhalation', 1),
(30, '01/01/1970 00:10:00', 'X-ray examination', 2);

insert into orderr (cost, finish, start, doctor_id, patient_id, procedure_id) VALUES
(50, '29/07/2021 10:00:00', '29/07/2021 10:10:00', 1, 3, 2);