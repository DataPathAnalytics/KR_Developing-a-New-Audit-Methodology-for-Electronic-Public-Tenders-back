--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_report_cpv();

CREATE OR REPLACE FUNCTION calc_report_cpv()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE report_cpv;
  INSERT INTO report_cpv
  SELECT DISTINCT ON (t.buyer_id,
                     i.classification_id,
                     EXTRACT(YEAR FROM t.date_published)) t.buyer_id,
                     i.classification_id,
                     t.date,
                     EXTRACT(YEAR FROM t.date_published)
  FROM tender t
         JOIN item i ON t.id = i.tender_id
         join lot l on i.lot_id = l.id
  WHERE t.bad_quality is false
    and t.status = 'complete'
    and t.procurement_method_details = 'singleSource'
    and l.status in ('complete', 'active')
  ORDER BY t.buyer_id, i.classification_id, EXTRACT(YEAR FROM t.date_published), t.date;
END;
$$
LANGUAGE 'plpgsql';