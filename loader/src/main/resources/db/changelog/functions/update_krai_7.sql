--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_7()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _related_process       RECORD;
  _previous_tender       RECORD;
  _current_suppliers     INTEGER [];
  _previous_suppliers    INTEGER [];

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 7;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;
BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'singleSource'
    AND t.status = 'complete'
    AND t.procurement_method_rationale = 'additionalProcurement25'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND t.status = 'complete'
                   AND t.procurement_method_rationale = 'additionalProcurement25'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    SELECT *
    FROM related_process
    WHERE tender_id = _tender.id
      AND relationship = 'prior' INTO _related_process;

    IF _related_process IS NULL
    THEN
      INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, INCORRECT);
      CONTINUE;
    END IF;

    SELECT * FROM tender WHERE id = _related_process.identifier INTO _previous_tender;

    IF _previous_tender.procurement_method_details NOT IN ('oneStage', 'downgrade', 'simplicated') OR
       _previous_tender.current_stage <> 'contractSigned'
    THEN
      INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, INCORRECT);
      CONTINUE;
    END IF;

    SELECT array_agg(party_id) FROM tender_supplier WHERE tender_id = _tender.id INTO _current_suppliers;
    SELECT array_agg(party_id) FROM tender_supplier WHERE tender_id = _previous_tender.id INTO _previous_suppliers;

    IF _previous_suppliers @> _current_suppliers IS FALSE THEN
      _indicator_value = PASSED;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';