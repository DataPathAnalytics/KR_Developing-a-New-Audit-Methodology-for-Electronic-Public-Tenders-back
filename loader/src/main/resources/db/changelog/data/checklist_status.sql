--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true


INSERT INTO checklist_status(id, name) VALUES (1, 'active')
ON CONFLICT DO NOTHING;

INSERT INTO checklist_status(id, name) VALUES (2, 'completed')
ON CONFLICT DO NOTHING;

UPDATE checklist_status SET name = 'активный' WHERE id = 1;

UPDATE checklist_status SET name = 'завершенный' WHERE id = 2;

UPDATE checklist_status SET name_en = 'active' WHERE id = 1;
UPDATE checklist_status SET name_en = 'completed' WHERE id = 2;