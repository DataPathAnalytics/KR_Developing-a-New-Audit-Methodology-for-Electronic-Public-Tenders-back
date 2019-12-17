--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true


INSERT INTO permission(id, name, description)
VALUES (1, 'admin.base', 'Администратор')
ON CONFLICT DO NOTHING;

INSERT INTO permission(id, name, description)
VALUES (2, 'auditor.base', 'Аудитор')
ON CONFLICT DO NOTHING;

-- Predefined permissions for admin and default auditor

INSERT INTO auditor_permission
VALUES (1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO auditor_permission
VALUES (2, 2)
ON CONFLICT DO NOTHING;

UPDATE permission SET description_en = 'Administrator', description = 'Администратор' WHERE id = 1;
UPDATE permission SET description_en = 'Auditor' WHERE id = 2;