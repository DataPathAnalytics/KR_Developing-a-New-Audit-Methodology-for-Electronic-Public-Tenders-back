--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION calc_pe_supplier()
  RETURNS VOID AS $$

DECLARE
  _tender RECORD;
BEGIN

  TRUNCATE pe_supplier;

  FOR _tender IN SELECT DISTINCT ON (t.id) t.*
                 FROM tender t
                        JOIN award a ON t.id = a.tender_id
                 WHERE t.procurement_method_details IN ('oneStage', 'simplicated', 'downgrade')
                   AND (t.status = 'complete' OR (t.status = 'active' AND t.current_stage = 'evaluationComplete' AND
                                                  EXTRACT(DAY FROM now() - a.date) > 30))
                   AND t.bad_quality IS FALSE
                 ORDER BY t.id, a.date
  LOOP

    INSERT INTO pe_supplier
    SELECT _tender.buyer_id, p.id, _tender.date
    from tender_supplier ts
           JOIN party p on ts.party_id = p.id
    WHERE ts.tender_id = _tender.id;

    END LOOP;

END;
$$
  LANGUAGE 'plpgsql';