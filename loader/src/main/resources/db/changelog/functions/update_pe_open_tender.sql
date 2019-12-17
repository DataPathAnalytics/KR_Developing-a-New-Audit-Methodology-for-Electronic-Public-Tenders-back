--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_pe_open_tenders();

CREATE OR REPLACE FUNCTION calc_pe_open_tenders()
  RETURNS void AS $$
BEGIN
  TRUNCATE pe_open_tenders;

  INSERT INTO pe_open_tenders
  select distinct on (t.buyer_id,
                     i.classification_id,
                     t.date_published) t.buyer_id,
                     i.classification_id,
                     t.date_published
  from tender t
         join item i on t.id = i.tender_id
  where t.status in ('active', 'complete')
    and t.procurement_method_details in ('oneStage', 'simplicated', 'downgrade')
    and t.buyer_id is not null
    and t.bad_quality is false
    and i.classification_id is not null
  order by t.buyer_id, i.classification_id, t.date_published;

END;
$$
LANGUAGE 'plpgsql';