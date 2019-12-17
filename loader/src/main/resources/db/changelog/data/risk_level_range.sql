--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (1, 'Очень низкий', 0, 1, 'oneStage', 1)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (2, 'Низкий', 2, 2, 'oneStage', 3)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (3, 'Средний', 4, 3, 'oneStage', 5)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (4, 'Высокий', 6, 4, 'oneStage', 8)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (5, 'Очень высокий', 9, 5, 'oneStage', 100)
ON CONFLICT DO NOTHING;

-- simplicated
INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (6, 'Очень низкий', 0, 1, 'simplicated', 1)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (7, 'Низкий', 2, 2, 'simplicated', 3)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (8, 'Средний', 4, 3, 'simplicated', 5)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (9, 'Высокий', 6, 4, 'simplicated', 8)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (10, 'Очень высокий', 9, 5, 'simplicated', 100)
ON CONFLICT DO NOTHING;


-- downgrade
INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (11, 'Очень низкий', 0, 1, 'downgrade', 1)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (12, 'Низкий', 2, 2, 'downgrade', 3)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (13, 'Средний', 4, 3, 'downgrade', 5)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (14, 'Высокий', 6, 4, 'downgrade', 8)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (15, 'Очень высокий', 9, 5, 'downgrade', 100)
ON CONFLICT DO NOTHING;


-- singleSource
INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (16, 'Очень низкий', 0, 1, 'singleSource', 1)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (17, 'Низкий', 2, 2, 'singleSource', 3)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (18, 'Средний', 4, 3, 'singleSource', 5)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (19, 'Высокий', 6, 4, 'singleSource', 8)
ON CONFLICT DO NOTHING;

INSERT INTO tender_risk_level_range(id, description, left_bound, level, procurement_method, right_bound)
VALUES (20, 'Очень высокий', 9, 5, 'singleSource', 100)
ON CONFLICT DO NOTHING;

-- buyer levels
INSERT INTO buyer_risk_level_range(id, description, left_bound, right_bound, level)
VALUES (1, 'Очень низкий', 0, 2, 1)
ON CONFLICT DO NOTHING;

INSERT INTO buyer_risk_level_range(id, description, left_bound, right_bound, level)
VALUES (2, 'Низкий', 3, 6, 2)
ON CONFLICT DO NOTHING;

INSERT INTO buyer_risk_level_range(id, description, left_bound, right_bound, level)
VALUES (3, 'Средний', 7, 14, 3)
ON CONFLICT DO NOTHING;

INSERT INTO buyer_risk_level_range(id, description, left_bound, right_bound, level)
VALUES (4, 'Высокий', 15, 20, 4)
ON CONFLICT DO NOTHING;

INSERT INTO buyer_risk_level_range(id, description, left_bound, right_bound, level)
VALUES (5, 'Очень Высокий', 21, 100, 5)
ON CONFLICT DO NOTHING;

UPDATE buyer_risk_level_range SET description_en = 'Very Low' WHERE id = 1;
UPDATE buyer_risk_level_range SET description_en = 'Low' WHERE id = 2;
UPDATE buyer_risk_level_range SET description_en = 'Average' WHERE id = 3;
UPDATE buyer_risk_level_range SET description_en = 'High' WHERE id = 4;
UPDATE buyer_risk_level_range SET description_en = 'Very High' WHERE id = 5;