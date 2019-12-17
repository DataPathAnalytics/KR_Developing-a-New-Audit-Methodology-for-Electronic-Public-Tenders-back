--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_17()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _award_amount          double precision;
  _contract_amount       double precision;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 17;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details IN ('singleSource', 'oneStage', 'downgrade', 'simplicated')
    AND t.status = 'complete'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details IN ('singleSource', 'oneStage', 'downgrade', 'simplicated')
                   AND t.status = 'complete'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    SELECT sum(value_amount) FROM award WHERE tender_id = _tender.id AND status = 'active' INTO _award_amount;
    SELECT sum(value_amount) FROM contract WHERE tender_id = _tender.id AND status = 'active' INTO _contract_amount;

    IF _award_amount - 0.5 < _contract_amount AND _award_amount + 0.5 < _contract_amount THEN
      _indicator_value = PASSED;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';