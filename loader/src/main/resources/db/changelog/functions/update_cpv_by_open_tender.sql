--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_cpv_by_open_tender();

CREATE OR REPLACE FUNCTION calc_cpv_by_open_tender()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE cpv_by_open_tender;
  INSERT INTO cpv_by_open_tender
  select distinct on (i.classification_id,
                     EXTRACT(YEAR FROM t.date_published)) i.classification_id,
                     EXTRACT(YEAR FROM t.date_published)
  from tender t
         join award a on t.id = a.tender_id
         join lot l on a.lot_id = l.id
         join item i on l.id = i.lot_id
  where t.status = 'complete'
    and t.procurement_method_details in ('simplicated', 'downgrade', 'oneStage')
    and t.bad_quality is false
    and a.status = 'active'
  ORDER BY i.classification_id, EXTRACT(YEAR FROM t.date_published), t.date_published;
END;
$$
LANGUAGE 'plpgsql';