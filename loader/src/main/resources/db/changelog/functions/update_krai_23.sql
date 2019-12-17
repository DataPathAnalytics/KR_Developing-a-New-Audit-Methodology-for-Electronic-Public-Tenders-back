--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_23()
  RETURNS void AS $$
declare
  _tender                RECORD;
  _cpv                   RECORD;
  _qr_count              integer;
  _avg_by_cpv            double precision;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 23;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;
BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT DISTINCT ON (t.id) t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
         JOIN award a ON t.id = a.tender_id
  WHERE t.procurement_method_details IN ('oneStage', 'simplicated', 'downgrade')
    AND (t.status = 'complete' OR (t.status = 'active' AND t.current_stage = 'evaluationComplete' AND
                                   EXTRACT(DAY FROM now() - a.date) > 30))
    AND t.bad_quality IS TRUE
    AND extract(years from t.date_published) >= extract(years from now()) - 1
  ORDER BY t.id, a.date;


  FOR _tender IN SELECT DISTINCT ON (t.id) t.*
                 FROM tender t
                        JOIN award a ON t.id = a.tender_id
                 WHERE t.procurement_method_details IN ('oneStage', 'simplicated', 'downgrade')
                   AND (t.status = 'complete' OR (t.status = 'active' AND t.current_stage = 'evaluationComplete' AND
                                                  EXTRACT(DAY FROM now() - a.date) > 30))
                   AND t.bad_quality IS FALSE
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                 ORDER BY t.id, a.date
  LOOP

    _indicator_value = FAILED;

    SELECT count(*) FROM qualification_requirement WHERE tender_id = _tender.id INTO _qr_count;
    IF _qr_count < 2
    THEN
      INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, INCORRECT);
      CONTINUE;
    END IF;

    FOR _cpv IN SELECT * FROM tender_cpv_list WHERE tender_id = _tender.id LOOP

      SELECT avg
      FROM cpv_qualif_requir
      WHERE cpv = _cpv.cpv
        AND year = extract(year from _tender.date_published) INTO _avg_by_cpv;

      IF _avg_by_cpv + _avg_by_cpv * 0.4 <= _qr_count
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