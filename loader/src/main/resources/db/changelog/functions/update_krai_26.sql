--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_26()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _tender_data           RECORD;
  _cpv_table_sum         DOUBLE PRECISION;
  _result_sum            DOUBLE PRECISION;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 26;
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
    AND t.procurement_method_rationale = 'annualProcurement'
    AND t.bad_quality IS TRUE;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND t.status = 'complete'
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.procurement_method_rationale = 'annualProcurement'
                   AND t.bad_quality IS FALSE
  LOOP

    _indicator_value = FAILED;

    FOR _tender_data IN SELECT _tender.buyer_id                           as buyer_id,
                               p.id                                       as supplier_id,
                               substring(i.classification_id, 1, 6)       as classification_code,
                               sum(i.quantity * pp.unit_value_amount)     as amount,
                               _tender.date                               as date,
                               EXTRACT(YEARS FROM _tender.date_published) as year
                        FROM award a
                               JOIN bid b on a.bid_id = b.id
                               JOIN price_proposal pp on pp.bid_id = b.id
                               JOIN item i on i.id = pp.item_id
                               JOIN party p ON p.outer_id = b.bidder_id
                        WHERE a.tender_id = _tender.id
                          AND a.status = 'active'
                        group by buyer_id, supplier_id, classification_code, date, year
    LOOP

      SELECT sum(amount)
      FROM cpv_one_supplier
      WHERE buyer_id = _tender_data.buyer_id
        AND supplier_id = _tender_data.supplier_id
        AND classification_code = _tender_data.classification_code
        AND date < _tender_data.date
        AND year = _tender_data.year
          INTO _cpv_table_sum;

      _result_sum = _cpv_table_sum + _tender_data.amount;

      IF _result_sum > 1000000
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