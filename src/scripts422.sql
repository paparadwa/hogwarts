create table people (
    name text primary key,
    age int,
    has_licence boolean
);

create table cars (
    mark text,
    model text,
    cost decimal,
    constraint pk primary key (mark, model)
);

create table car_users (
    name text references people (name),
    car_mark text,
    car_model text,
    foreign key (car_mark, car_model) references cars(mark, model)
);