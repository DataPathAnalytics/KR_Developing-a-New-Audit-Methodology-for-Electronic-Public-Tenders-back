--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_25()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;
  _qr_type               RECORD;

  _cpv_licence           TEXT [];
  _tender_cpv            TEXT [];

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 25;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details in ('singleSource', 'oneStage', 'downgrade', 'simplicated')
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE
    AND (t.status = 'complete'
           OR (t.status = 'active' AND
               t.current_stage = 'evaluationComplete' AND
               (SELECT (EXTRACT(DAYS FROM now() - date) > 30)
                FROM award
                WHERE tender_id = t.id
                ORDER BY date
                LIMIT 1)
             )
            );

  SELECT array_agg(cpv) FROM cpv_licence INTO _cpv_licence;

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details in ('singleSource', 'oneStage', 'downgrade', 'simplicated')
                   AND extract(years from t.date_published) >= extract(years from now()) - 1
                   AND t.bad_quality IS FALSE
                   AND (t.status = 'complete'
                          OR (t.status = 'active' AND
                              t.current_stage = 'evaluationComplete' AND
                              (SELECT (EXTRACT(DAYS FROM now() - date) > 30)
                               FROM award
                               WHERE tender_id = t.id
                               ORDER BY date
                               LIMIT 1)
                            )
                           )
  LOOP

    _indicator_value = FAILED;

    SELECT *
    FROM qualification_requirement
    WHERE tender_id = _tender.id
      AND type = 'Лицензия, выданная уполномоченным органом' INTO _qr_type;

    IF _qr_type IS NULL
    THEN

      SELECT array_agg(cpv) FROM tender_cpv_list WHERE tender_id = _tender.id INTO _tender_cpv;

      IF _cpv_licence && _tender_cpv IS TRUE THEN
        _indicator_value = PASSED;
      END IF;

    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';