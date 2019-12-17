--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

UPDATE checklist_score SET name_en = 'No deficiencies' WHERE id = 1;
UPDATE checklist_score SET name_en = 'Minor deficiencies' WHERE id = 2;
UPDATE checklist_score SET name_en = 'Moderate deficiencies' WHERE id = 3;
UPDATE checklist_score SET name_en = 'Major deficiencies' WHERE id = 4;
UPDATE checklist_score SET name_en = 'Extreme deficiencies' WHERE id = 5;
UPDATE checklist_score SET name_en = 'Critical deficiencies' WHERE id = 6;