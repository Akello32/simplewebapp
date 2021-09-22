create table IF NOT EXISTS department
(
    department_id serial  not null
        constraint department_pk
            primary key,
    name          varchar not null
);

alter table department
    owner to root;

create table IF NOT EXISTS employee
(
    employee_id   serial  not null
        constraint employee_pk
            primary key,
    first_name    varchar not null,
    last_name     varchar not null,
    job_title     varchar not null,
    gender        varchar not null,
    date_of_birth date,
    department_id integer not null
            references department
            on update cascade on delete cascade
);

alter table employee
    owner to root;

