--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION calc_buyer_prioritization()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE buyer_prioritization;

  INSERT INTO buyer_prioritization
  SELECT p.id,
         p.identifier_id,
         p.identifier_legal_name_ru,
         COUNT(t.id) FILTER (WHERE t.bad_quality IS TRUE) procedures_with_bad_data,
         p.region
  FROM party p
         JOIN tender t ON t.buyer_id = p.id
  WHERE (t.status = 'complete'
           OR (t.status = 'active' AND
               t.current_stage = 'evaluationComplete' AND
               (SELECT (EXTRACT(DAYS FROM now() - date) > 30) FROM award WHERE tender_id = t.id ORDER BY date LIMIT 1)
             )
            )
  GROUP BY p.id;
END;
$$
LANGUAGE 'plpgsql';