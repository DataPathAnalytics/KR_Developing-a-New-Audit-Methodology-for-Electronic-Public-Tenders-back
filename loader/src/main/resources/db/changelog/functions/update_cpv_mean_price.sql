--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_cpv_mean_price();

CREATE OR REPLACE FUNCTION calc_cpv_mean_price()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE cpv_mean_price;

  WITH tenders AS (SELECT id, EXTRACT(YEAR FROM date_published) AS year
                   FROM tender t
                   WHERE t.procurement_method_details IN ('oneStage', 'simplicated', 'downgrade')
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
                     AND t.bad_quality IS FALSE),
       result AS (SELECT i.classification_id, i.unit_id, t.year, (SELECT price_proposal.unit_value_amount
                                                                  FROM price_proposal
                                                                  WHERE bid_id = a.bid_id
                                                                    AND lot_id = l.id
                                                                    AND item_id = i.id) pp_amount
                  FROM tenders t
                         JOIN award a ON a.tender_id = t.id
                         JOIN lot l ON a.lot_id = l.id
                         JOIN item i ON l.id = i.lot_id
                  WHERE a.status = 'active'
                    AND l.status IN ('complete', 'active'))
  INSERT INTO cpv_mean_price
  SELECT classification_id, unit_id, avg(pp_amount), year
  FROM result
  GROUP BY classification_id, unit_id, year
  HAVING (count(*) > 3);

END;
$$
LANGUAGE 'plpgsql';