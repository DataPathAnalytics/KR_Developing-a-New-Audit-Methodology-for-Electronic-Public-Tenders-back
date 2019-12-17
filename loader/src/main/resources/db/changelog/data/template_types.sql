--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true

INSERT INTO template_type
VALUES (1, 'Оценка закупающей организации')
ON CONFLICT DO NOTHING;

INSERT INTO template_type
VALUES (2, 'Двухэтапные торги')
ON CONFLICT DO NOTHING;

INSERT INTO template_type
VALUES (3, 'Одноэтапные торги')
ON CONFLICT DO NOTHING;

INSERT INTO template_type
VALUES (4, 'Упрощенные торги')
ON CONFLICT DO NOTHING;

INSERT INTO template_type
VALUES (5, 'Метод прямого заключения договора')
ON CONFLICT DO NOTHING;

UPDATE template_type SET name_en = 'Procuring Entity Assessment' WHERE id = 1;
UPDATE template_type SET name_en = 'Two-stage Bidding' WHERE id = 2;
UPDATE template_type SET name_en = 'Single-stage Bidding' WHERE id = 3;
UPDATE template_type SET name_en = 'Simplified Acquisition' WHERE id = 4;
UPDATE template_type SET name_en = 'Direct contracting' WHERE id = 5;