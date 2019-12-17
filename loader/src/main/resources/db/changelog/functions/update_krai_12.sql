--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_12()
  RETURNS VOID AS $$
DECLARE
  _tender                RECORD;

  _indicator_value       integer;

  _INDICATOR_ID CONSTANT integer := 12;
  PASSED CONSTANT        integer := 1;
  INCORRECT CONSTANT     integer := -1;
  FAILED CONSTANT        integer := 0;

BEGIN

  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  INSERT INTO tender_indicator
  SELECT t.id, _INDICATOR_ID, INCORRECT
  FROM tender t
  WHERE t.procurement_method_details = 'oneStage'
    AND extract(years from t.date_published) >= extract(years from now()) - 1
    AND t.bad_quality IS TRUE
    AND (t.status = 'complete'
           OR (t.status = 'active' AND
               t.current_stage = 'evaluationComplete' AND
               (SELECT (EXTRACT(DAYS FROM now() - date) > 30) FROM award WHERE tender_id = t.id ORDER BY date LIMIT 1)
             )
            );

  FOR _tender IN SELECT *
                 FROM tender t
                 WHERE t.procurement_method_details = 'oneStage'
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

    IF _tender.has_documents IS FALSE THEN
      _indicator_value = PASSED;
    END IF;

    INSERT INTO tender_indicator VALUES (_tender.id, _INDICATOR_ID, _indicator_value);

  END LOOP;

END;
$$
LANGUAGE 'plpgsql';