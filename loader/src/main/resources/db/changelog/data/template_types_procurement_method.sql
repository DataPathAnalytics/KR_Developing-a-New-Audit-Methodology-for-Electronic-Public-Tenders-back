--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO template_type_procurement_method
VALUES (3, 'oneStage')
ON CONFLICT DO NOTHING;

INSERT INTO template_type_procurement_method
VALUES (3, 'egov')
ON CONFLICT DO NOTHING;

INSERT INTO template_type_procurement_method
VALUES (4, 'simplicated')
ON CONFLICT DO NOTHING;

INSERT INTO template_type_procurement_method
VALUES (4, 'downgrade')
ON CONFLICT DO NOTHING;

INSERT INTO template_type_procurement_method
VALUES (5, 'singleSource')
ON CONFLICT DO NOTHING;