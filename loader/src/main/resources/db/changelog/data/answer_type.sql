--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

UPDATE answer_type SET name_en = 'Yes' WHERE id = 1;
UPDATE answer_type SET name_en = 'No' WHERE id = 2;
UPDATE answer_type SET name_en = 'Not applicable' WHERE id = 3;