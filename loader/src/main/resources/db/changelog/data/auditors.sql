--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true


INSERT INTO auditor(email, password, name, disabled)
VALUES ('admin@datapath.com', '$2a$10$Hvl.cwZe6rBzGNkLGf7wPuUM70hQlBGpjc1ZTSgG3jtipFps9FVNm', 'ADMIN', false)
ON CONFLICT DO NOTHING;

INSERT INTO auditor(email, password, name, disabled)
VALUES ('auditor@datapath.com', '$2a$10$Hvl.cwZe6rBzGNkLGf7wPuUM70hQlBGpjc1ZTSgG3jtipFps9FVNm', 'AUDITOR', false)
ON CONFLICT DO NOTHING;