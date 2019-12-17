--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION calc_cpv_one_supplier()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE cpv_one_supplier;
  INSERT INTO cpv_one_supplier
  SELECT t.buyer_id                             as buyer_id,
         p.id                                   as supplier_id,
         substring(i.classification_id, 1, 6)   as classification_code,
         sum(i.quantity * pp.unit_value_amount) as amount,
         t.date,
         EXTRACT(YEARS FROM t.date_published)   as year
  from tender t
         join award a on t.id = a.tender_id
         join bid b on a.bid_id = b.id
         join price_proposal pp on b.id = pp.bid_id
         join item i on pp.item_id = i.id
         join party p on p.outer_id = b.bidder_id
  where procurement_method_rationale = 'annualProcurement'
    and t.status = 'complete'
    and a.status = 'active'
    and t.bad_quality is false
    and EXTRACT(YEARS FROM t.date) >= EXTRACT(YEARS FROM now()) - 1
  group by buyer_id, supplier_id, classification_code, t.date, year;
END;
$$
LANGUAGE 'plpgsql';