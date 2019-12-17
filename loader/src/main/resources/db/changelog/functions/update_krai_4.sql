--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_4()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _related_process       RECORD;
  _previous_tender       RECORD;
  _cpv_amount            RECORD;
  _previous_amount       DOUBLE PRECISION [];
  _current_cpv_list      TEXT [];
  _previous_cpv_list     TEXT [];

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 4;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT integer := -1;
  FAILED CONSTANT    integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'singleSource'
    AND t.status = 'complete'
    AND t.procurement_method_rationale = 'additionalProcurement10'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND t.status = 'complete'
                   AND t.procurement_method_rationale = 'additionalProcurement10'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    SELECT *
    FROM related_process
    WHERE tender_id = _tender.id
      AND relationship = 'prior'
    LIMIT 1 INTO _related_process;

    IF _related_process IS NULL OR _related_process.identifier IS NULL
    THEN
      INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, INCORRECT);
      CONTINUE;
    END IF;

    FOR _related_process IN SELECT * FROM related_process WHERE tender_id = _tender.id
                                                            AND relationship = 'prior'
    LOOP

      SELECT * FROM tender WHERE id = _related_process.identifier INTO _previous_tender;

      IF _previous_tender.procurement_method_details NOT IN ('oneStage', 'downgrade', 'simplicated') OR
         _previous_tender.current_stage NOT IN ('contractSigned', 'evaluationComplete')
      THEN
        _indicator_value = INCORRECT;
        EXIT;
      END IF;

      SELECT array_agg(cpv) FROM tender_cpv_list WHERE tender_id = _previous_tender.id INTO _previous_cpv_list;
      SELECT array_agg(cpv) FROM tender_cpv_list WHERE tender_id = _tender.id INTO _current_cpv_list;

      IF _previous_cpv_list IS NULL OR _previous_cpv_list @> _current_cpv_list IS FALSE
      THEN
        _indicator_value = PASSED;
        EXIT;
      END IF;

      FOR _cpv_amount IN SELECT i.classification_id, pp.unit_value_amount
                         FROM price_proposal pp
                                JOIN item i ON pp.item_id = i.id
                                JOIN lot l ON i.lot_id = l.id
                                JOIN award a on l.id = a.lot_id
                                JOIN bid b on a.bid_id = b.id
                         WHERE i.tender_id = _tender.id
                           AND a.status = 'active'
                           AND pp.lot_id = l.id
                           AND pp.item_id = i.id
                           AND pp.bid_id = b.id
      LOOP

        SELECT array_agg(pp.unit_value_amount)
        FROM price_proposal pp
               JOIN item i ON pp.item_id = i.id
               JOIN lot l ON i.lot_id = l.id
               JOIN award a on l.id = a.lot_id
               JOIN bid b on a.bid_id = b.id
        WHERE i.tender_id = _previous_tender.id
          AND i.classification_id = _cpv_amount.classification_id
          AND a.status = 'active'
          AND pp.lot_id = l.id
          AND pp.item_id = i.id
          AND pp.bid_id = b.id
            INTO _previous_amount;

        IF _previous_amount IS NULL OR _previous_amount @> array_agg(_cpv_amount.unit_value_amount) IS FALSE
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