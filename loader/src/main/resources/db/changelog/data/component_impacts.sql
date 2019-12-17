--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true

INSERT INTO component_impact VALUES(1,'Отсутствует',0) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(2,'Очень низкий',1) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(3,'Низкий',2) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(4,'Средний',3) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(5,'Высокий',4) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(6,'Очень высокий',5) ON CONFLICT DO NOTHING;
INSERT INTO component_impact VALUES(7,'Критический',6) ON CONFLICT DO NOTHING;

UPDATE component_impact SET name_en = 'Absent' WHERE id = 1;
UPDATE component_impact SET name_en = 'Very Low' WHERE id = 2;
UPDATE component_impact SET name_en = 'Low' WHERE id = 3;
UPDATE component_impact SET name_en = 'Medium' WHERE id = 4;
UPDATE component_impact SET name_en = 'High' WHERE id = 5;
UPDATE component_impact SET name_en = 'Very High' WHERE id = 6;
UPDATE component_impact SET name_en = 'Critical' WHERE id = 7;