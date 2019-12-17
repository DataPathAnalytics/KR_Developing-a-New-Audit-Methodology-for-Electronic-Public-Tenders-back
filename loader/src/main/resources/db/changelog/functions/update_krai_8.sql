--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_8()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _previous_tender       RECORD;
  _related_process       RECORD;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 8;
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
    AND t.procurement_method_rationale = 'additionalProcurement25'
    AND t.status = 'complete';

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
                   AND t.procurement_method_rationale = 'additionalProcurement25'
                   AND t.status = 'complete'
  LOOP

    _indicator_value = FAILED;

    SELECT *
    FROM related_process
    WHERE tender_id = _tender.id
      AND relationship = 'prior'
      AND identifier IS NOT NULL INTO _related_process;

    IF _related_process IS NULL
    THEN
      _indicator_value = PASSED;
    ELSE
      SELECT * FROM tender WHERE id = _related_process.identifier INTO _previous_tender;

      IF (_previous_tender.procurement_method_details NOT IN ('oneStage', 'downgrade', 'simplicated')) OR
         (_previous_tender.current_stage <> 'contractSigned')
      THEN
        _indicator_value = PASSED;
      END IF;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';