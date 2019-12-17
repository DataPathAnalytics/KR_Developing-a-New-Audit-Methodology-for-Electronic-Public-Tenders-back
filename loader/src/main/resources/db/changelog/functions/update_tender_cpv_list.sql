--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION calc_tender_cpv_list()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE tender_cpv_list;
  INSERT INTO tender_cpv_list
  SELECT distinct on (t.id, i.classification_id) t.id, i.classification_id
  from tender t
         join item i on t.id = i.tender_id
         join lot l on i.lot_id = l.id
  where l.status in ('complete', 'active')
    and t.bad_quality is false;
END;
$$
LANGUAGE 'plpgsql';