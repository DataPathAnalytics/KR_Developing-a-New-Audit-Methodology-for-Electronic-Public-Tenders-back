--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_2()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _cpv_list              text [];
  _report_cpv            RECORD;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 2;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'singleSource'
    AND t.bad_quality IS TRUE
    AND t.date_published >= '2018-01-01'
    AND t.status = 'complete';

  FOR _tender IN SELECT t.id, t.date, t.buyer_id, t.date_published
                 FROM tender t
                 WHERE t.procurement_method_details = 'singleSource'
                   AND t.date_published >= '2018-01-01'
                   AND t.bad_quality IS FALSE
                   AND t.status = 'complete'
  LOOP
    _indicator_value = FAILED;

    SELECT array_agg(cpv) FROM tender_cpv_list WHERE tender_id = _tender.id INTO _cpv_list;

    FOR _report_cpv IN SELECT cpv, date
                       FROM report_cpv
                       WHERE buyer_id = _tender.buyer_id
                         AND published_year = EXTRACT(YEARS FROM _tender.date_published)
                         AND _tender.date > report_cpv.date
    LOOP

      IF _cpv_list @> array_agg(_report_cpv.cpv) IS TRUE AND _tender.date > _report_cpv.date
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