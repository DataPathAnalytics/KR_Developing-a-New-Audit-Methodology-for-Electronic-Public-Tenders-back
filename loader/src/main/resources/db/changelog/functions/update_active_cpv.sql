--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_active_cpv();

CREATE OR REPLACE FUNCTION calc_active_cpv()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE active_cpv;
  INSERT INTO active_cpv
  SELECT DISTINCT ON (t.buyer_id,
                     i.classification_id,
                     EXTRACT(YEAR FROM t.date_published)) t.buyer_id,
                     i.classification_id,
                     t.date,
                     EXTRACT(YEAR FROM t.date_published)
  FROM tender t
         JOIN item i ON i.tender_id = t.id
         join lot l on i.lot_id = l.id
  WHERE t.procurement_method_details in ('simplicated', 'downgrade', 'oneStage')
    and t.current_stage in ('bidsOpened', 'evaluationResultsPending')
    and l.status in ('active', 'complete')
    and t.buyer_id is not null
  ORDER BY t.buyer_id, i.classification_id, EXTRACT(YEAR FROM t.date_published), t.date;
END;
$$
LANGUAGE 'plpgsql';