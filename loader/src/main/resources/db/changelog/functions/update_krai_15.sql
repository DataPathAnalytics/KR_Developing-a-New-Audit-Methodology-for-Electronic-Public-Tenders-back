--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_15()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _contract              RECORD;
  _award_id              integer;
  _award_date            DATE;
  _work_day_count        integer;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 15;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'oneStage'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE
    AND t.status = 'complete';

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'oneStage'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
                   AND t.status = 'complete'
  LOOP

    _indicator_value = FAILED;

    FOR _contract IN SELECT id, date_signed FROM contract WHERE tender_id = _tender.id
                                                            AND status = 'active' LOOP

      FOR _award_id IN SELECT award_id FROM contract_award WHERE contract_id = _contract.id LOOP

        SELECT date FROM award WHERE id = _award_id INTO _award_date;

        IF _contract.date_signed <= _award_date
        THEN
          _indicator_value = PASSED;
          EXIT;
        END IF;

        SELECT count(*)
        FROM work_calendar
        WHERE is_working IS TRUE
          AND date > _award_date
          AND date <= _contract.date_signed INTO _work_day_count;

        IF _work_day_count <= 7
        THEN
          _indicator_value = PASSED;
          EXIT;
        END IF;

      END LOOP;

      IF _indicator_value = PASSED
      THEN
        EXIT;
      END IF;

    END LOOP;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';