alter table student add constraint age_constraint check(age >= 16);

alter table student add constraint name_unique unique (name);
alter table student add constraint name_constraint check(name is not null);

alter table faculty add constraint faculty_and_color_unique unique (name, color);

alter table student alter column age set default 20;