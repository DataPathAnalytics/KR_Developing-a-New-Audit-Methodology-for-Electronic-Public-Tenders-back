--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_20()
  RETURNS void AS $$
declare
  _tender                RECORD;
  _buyer_cpv             text [];
  _tender_cpv            text [];

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 20;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;
BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.bad_quality IS TRUE
    AND t.procurement_method_rationale = 'urgentNeed'
    AND t.procurement_method_details = 'singleSource'
    AND t.status = 'complete'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND EXTRACT(DAYS FROM now() - t.date) > 30;


  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.bad_quality IS FALSE
                   AND t.procurement_method_rationale = 'urgentNeed'
                   AND t.procurement_method_details = 'singleSource'
                   AND t.status = 'complete'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND EXTRACT(DAYS FROM now() - t.date) > 30
  LOOP

    _indicator_value = FAILED;

    SELECT array_agg(cpv)
    FROM pe_open_tenders
    WHERE buyer_id = _tender.buyer_id
      AND date > _tender.date
      AND EXTRACT(DAYS FROM date - _tender.date) <= 30
        INTO _buyer_cpv;

    SELECT array_agg(cpv) FROM tender_cpv_list WHERE tender_id = _tender.id INTO _tender_cpv;

    IF _buyer_cpv IS NULL OR _buyer_cpv @> _tender_cpv IS FALSE THEN
      _indicator_value = PASSED;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';