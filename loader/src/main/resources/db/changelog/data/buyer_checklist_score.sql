--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

UPDATE checklist_score SET buyer_left_bound = 0, buyer_right_bound = 1 WHERE id = 1;
UPDATE checklist_score SET buyer_left_bound = 1, buyer_right_bound = 2 WHERE id = 2;
UPDATE checklist_score SET buyer_left_bound = 2, buyer_right_bound = 3 WHERE id = 3;
UPDATE checklist_score SET buyer_left_bound = 3, buyer_right_bound = 4 WHERE id = 4;
UPDATE checklist_score SET buyer_left_bound = 4, buyer_right_bound = 5 WHERE id = 5;
UPDATE checklist_score SET buyer_left_bound = 5, buyer_right_bound = 100 WHERE id = 6;