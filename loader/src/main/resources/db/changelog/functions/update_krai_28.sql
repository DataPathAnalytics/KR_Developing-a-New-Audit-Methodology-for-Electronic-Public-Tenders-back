--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_28()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _supplier_by_tender    RECORD;

  _indicator_value       integer;
  _buyers_ids            integer [];

  _INDICATOR_ID CONSTANT integer := 28;
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
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND t.status = 'complete'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    FOR _supplier_by_tender IN SELECT * FROM tender_supplier ts WHERE ts.tender_id = _tender.id
    LOOP

      SELECT array_agg(DISTINCT buyer_id)
      FROM pe_supplier
      WHERE supplier_id = _supplier_by_tender.party_id
        AND date < _tender.date_published INTO _buyers_ids;

      IF array_length(_buyers_ids, 1) = 1
      THEN

        IF _buyers_ids [1] = _tender.buyer_id
        THEN
          _indicator_value = PASSED;
          EXIT;
        END IF;

      END IF;

    END LOOP;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';