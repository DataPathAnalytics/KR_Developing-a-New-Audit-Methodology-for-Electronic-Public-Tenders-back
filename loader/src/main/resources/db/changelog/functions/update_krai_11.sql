--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_11()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _item                  RECORD;
  _pp_value_amount       DOUBLE PRECISION;
  _mean_price_amount     DOUBLE PRECISION;
  _result                DOUBLE PRECISION;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 11;
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

    FOR _item IN SELECT i.id, classification_id, unit_id, lot_id
                 FROM item i
                        JOIN lot l ON i.lot_id = l.id
                 WHERE i.tender_id = _tender.id
                   AND l.status IN ('complete', 'active')
    LOOP

      SELECT amount
      FROM cpv_mean_price
      WHERE cpv = _item.classification_id
        AND unit_id = _item.unit_id
        AND year = EXTRACT(YEAR FROM _tender.date_published) INTO _mean_price_amount;

      IF _mean_price_amount IS NULL
      THEN
        CONTINUE;
      END IF;

      FOR _pp_value_amount IN SELECT pp.unit_value_amount
                              FROM award a
                                     join bid b on a.bid_id = b.id
                                     join price_proposal pp on b.id = pp.bid_id
                              WHERE a.lot_id = _item.lot_id
                                AND a.status = 'active'
                                AND pp.item_id = _item.id
      LOOP

        _result = _pp_value_amount / _mean_price_amount * 100;

        IF _result > 120 OR _result < 80
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