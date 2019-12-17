--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_9()
  RETURNS void AS $$
DECLARE
  _tender                RECORD;
  _item_cpv              RECORD;
  _cpv                   TEXT;

  _indicator_value       integer;
  _tender_year           integer;

  _INDICATOR_ID CONSTANT integer := 9;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN
  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT id, _INDICATOR_ID, INCORRECT
  FROM tender
  WHERE procurement_method_rationale = 'intellectualRights'
  AND status = 'complete'
  AND procurement_method_details = 'singleSource'
  AND extract(years from date_published) >= extract(years from now()) - 1
  AND bad_quality IS TRUE;

  FOR _tender IN SELECT id, date_published
                 FROM tender
                 WHERE procurement_method_rationale = 'intellectualRights'
                 AND status = 'complete'
                 AND procurement_method_details = 'singleSource'
                 AND extract(years from date_published) >= extract(years from now()) - 1
                 AND bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    _tender_year = EXTRACT(YEAR FROM _tender.date_published);

    FOR _item_cpv IN SELECT classification_id FROM item WHERE tender_id = _tender.id
    LOOP

      SELECT cpv FROM cpv_by_open_tender WHERE year = _tender_year
                                           AND cpv = _item_cpv.classification_id
          INTO _cpv;

      IF _cpv IS NOT NULL
      THEN
        _indicator_value = PASSED;
        EXIT;
      END IF;

    END LOOP;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;
END;
$$
LANGUAGE 'plpgsql';