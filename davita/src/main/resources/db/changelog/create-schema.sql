BEGIN;


CREATE TABLE public.doctor
(
    certificate_number character varying(255) NOT NULL,
    specialization character varying(255) NOT NULL,
    doctor_id bigint NOT NULL,
    PRIMARY KEY (doctor_id)
);

CREATE TABLE public.equipment
(
    equipment_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    name character varying(255) NOT NULL,
    PRIMARY KEY (equipment_id)
);

CREATE TABLE public.orderr
(
    order_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    cost double precision NOT NULL,
    finish timestamp without time zone NOT NULL,
    start timestamp without time zone NOT NULL,
    doctor_id bigint,
    patient_id bigint,
    procedure_id bigint,
    PRIMARY KEY (order_id)
);

CREATE TABLE public.patient
(
    insurance_number character varying(255) NOT NULL,
    patient_id bigint NOT NULL,
    PRIMARY KEY (patient_id)
);

CREATE TABLE public.permission
(
    permission_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    name character varying(255) NOT NULL,
    PRIMARY KEY (permission_id)
);

CREATE TABLE public.procedure
(
    procedure_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    cost double precision NOT NULL,
    duration time without time zone NOT NULL,
    name character varying(255) NOT NULL,
    equipment_id bigint,
    PRIMARY KEY (procedure_id)
);

CREATE TABLE public.role
(
    role_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    name character varying(255) NOT NULL,
    PRIMARY KEY (role_id)
);

CREATE TABLE public.role_permission
(
    role_id bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE public.user_role
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

CREATE TABLE public.userr
(
    user_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (MINVALUE 1 START WITH 1 CACHE 1),
    date_of_birthday date,
    email character varying(255),
    last_name character varying(255) NOT NULL,
    login character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(255),
    PRIMARY KEY (user_id)
);

ALTER TABLE public.doctor
    ADD FOREIGN KEY (doctor_id)
        REFERENCES public.userr (user_id)
        NOT VALID;


ALTER TABLE public.orderr
    ADD FOREIGN KEY (doctor_id)
        REFERENCES public.doctor (doctor_id)
        NOT VALID;


ALTER TABLE public.orderr
    ADD FOREIGN KEY (procedure_id)
        REFERENCES public.procedure (procedure_id)
        NOT VALID;


ALTER TABLE public.orderr
    ADD FOREIGN KEY (patient_id)
        REFERENCES public.patient (patient_id)
        NOT VALID;


ALTER TABLE public.patient
    ADD FOREIGN KEY (patient_id)
        REFERENCES public.userr (user_id)
        NOT VALID;


ALTER TABLE public.procedure
    ADD FOREIGN KEY (equipment_id)
        REFERENCES public.equipment (equipment_id)
        NOT VALID;


ALTER TABLE public.role_permission
    ADD FOREIGN KEY (role_id)
        REFERENCES public.role (role_id)
        NOT VALID;


ALTER TABLE public.role_permission
    ADD FOREIGN KEY (permission_id)
        REFERENCES public.permission (permission_id)
        NOT VALID;


ALTER TABLE public.user_role
    ADD FOREIGN KEY (user_id)
        REFERENCES public.userr (user_id)
        NOT VALID;


ALTER TABLE public.user_role
    ADD FOREIGN KEY (role_id)
        REFERENCES public.role (role_id)
        NOT VALID;

END;
