select s.name, s.age, f.name from student s
inner join faculty f on f.id = s.faculty_id;

select s.name, s.age, f.name from student s
inner join faculty f on f.id = s.faculty_id
inner join avatar a on a.student_id = s.id;