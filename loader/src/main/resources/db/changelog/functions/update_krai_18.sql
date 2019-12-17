--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_18()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _award_date            DATE;
  _work_day_count        integer;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 18;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'singleSource'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE
    AND t.status = 'complete';

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
                   AND t.status = 'complete'
  LOOP

    _indicator_value = FAILED;

    SELECT date FROM award WHERE tender_id = _tender.id AND status = 'active' INTO _award_date;

    SELECT count(*)
    FROM work_calendar
    WHERE is_working IS TRUE
      AND date > _tender.date_published
      AND date <= _award_date INTO _work_day_count;

    IF _work_day_count <= 2 THEN
      _indicator_value = PASSED;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';