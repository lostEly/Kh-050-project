insert into role (name)
values ('ADMIN'),
       ('USER'),
       ('DOCTOR'),
       ('PATIENT');

insert into userr (date_of_birthday, email, last_name, login, name, password, phone)
VALUES ('01/01/1970', 'doctor@gmail.com', 'Petrov', 'docPetrov', 'Petr', 'docpass', '0997894532'),
       ('12/01/1975', 'patient1@gmail.com', 'Ivanov', 'patIvanov', 'Ivan', 'patpass1', '0667897732'),
       ('02/02/1980', 'patient2@gmail.com', 'Pupkin', 'patPupkin', 'Vasiliy', 'patpass2', '0957885232'),
       ('09/03/1993', 'patient3@gmail.com', 'Sidorov', 'patSidorov', 'Vladislav', 'patpass3', '0967885732'),
       ('08/09/1970', 'doctor1@gmail.com', 'Seleznev', 'docSeleznev', 'Victor', 'docpass1', '0977894532');

insert into user_role (user_id, role_id)
VALUES (1, 3),
       (2, 4),
       (3, 4),
       (4, 4),
       (5, 3);

insert into doctor (certificate_number, specialization, doctor_id)
VALUES (754841321813, 'surgeon', 1),
       (864834345613, 'therapist', 5);

insert into patient (insurance_number, patient_id)
VALUES (12151543215, 2),
       (12345677543, 3),
       (44667745465, 4);

insert into equipment (name)
values ('ultrasound'),
       ('x-ray'),
       ('office 23');

insert into procedure (cost, duration, name, equipment_id)
VALUES (50, '00:15:00', 'Ultrasound', 1),
       (30, '00:10:00', 'X-ray examination', 2),
       (20, '00:10:00', 'Appointment', 3);

insert into orderr (cost, finish, start, procedure_id)
VALUES (50, '09/08/2021 09:15:00', '09/08/2021 09:00:00', 1),
       (50, '09/08/2021 09:30:00', '09/08/2021 09:15:00', 1),
       (50, '09/08/2021 09:45:00', '09/08/2021 09:30:00', 1),
       (30, '09/08/2021 09:10:00', '09/08/2021 09:00:00', 2),
       (30, '09/08/2021 09:20:00', '09/08/2021 09:10:00', 2),
       (30, '09/08/2021 09:30:00', '09/08/2021 09:20:00', 2),
       (20, '09/08/2021 10:10:00', '09/08/2021 10:00:00', 3),
       (20, '09/08/2021 10:20:00', '09/08/2021 10:10:00', 3),
       (20, '09/08/2021 10:30:00', '09/08/2021 10:20:00', 3);