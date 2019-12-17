--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_report_one_time();

CREATE OR REPLACE FUNCTION calc_report_one_time()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE report_one_time;
  INSERT INTO report_one_time
  SELECT DISTINCT ON (t.buyer_id,
                     i.classification_id,
                     EXTRACT(YEAR FROM t.date_published)) t.buyer_id,
                     i.classification_id,
                     t.date,
                     EXTRACT(YEAR FROM t.date_published)
  FROM tender t
         JOIN item i ON t.id = i.tender_id
         JOIN lot l on i.lot_id = l.id
  WHERE t.procurement_method_rationale = 'annualProcurement'
    AND t.procurement_method_details = 'singleSource'
    AND t.status = 'complete'
    and t.bad_quality is false
    and l.status in ('complete', 'active')
  ORDER BY t.buyer_id, i.classification_id, EXTRACT(YEAR FROM t.date_published), t.date;
END;
$$
LANGUAGE 'plpgsql';