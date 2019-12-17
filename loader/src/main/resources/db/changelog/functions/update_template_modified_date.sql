--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true runAlways:false

CREATE OR REPLACE FUNCTION update_template_modified_date()
  RETURNS trigger AS
$$
BEGIN

  IF TG_TABLE_NAME = 'question_category' THEN
    IF tg_op = 'DELETE' THEN
      UPDATE template SET modified_date = CURRENT_DATE WHERE id = OLD.template_id;
      RETURN OLD;
    ELSE
      UPDATE template
      SET modified_date = CURRENT_DATE
      WHERE id = NEW.template_id;
      RETURN NEW;
    END IF;
  ELSEIF TG_TABLE_NAME = 'question' THEN
    IF tg_op = 'DELETE' THEN
      UPDATE template
      SET modified_date = CURRENT_DATE
      WHERE id = (
        SELECT template_id
        FROM question_category
        WHERE id = OLD.category_id
      );
      RETURN OLD;
    ELSE
      UPDATE template
      SET modified_date = CURRENT_DATE
      WHERE id = (
        SELECT template_id
        FROM question_category
        WHERE id = NEW.category_id
      );
      RETURN NEW;
    END IF;
  END IF;

END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS category_update_template_modified_date ON question_category;

CREATE TRIGGER  category_update_template_modified_date
  AFTER INSERT OR UPDATE OR DELETE
  on question_category
  FOR EACH ROW
EXECUTE PROCEDURE update_template_modified_date();

DROP TRIGGER IF EXISTS question_update_template_modified_date ON question;

CREATE TRIGGER question_update_template_modified_date
  AFTER INSERT OR UPDATE OR DELETE
  on question
  FOR EACH ROW
EXECUTE PROCEDURE update_template_modified_date();