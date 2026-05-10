-- liquibase formatted sql

-- changeset atimoshkov:1
CREATE INDEX student_name_index ON student (name);

-- changeset atimoshkov:2
CREATE INDEX faculty_name_and_color_index ON faculty (name, color);