--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

DROP FUNCTION IF EXISTS calc_cpv_qualif_requir();

CREATE OR REPLACE FUNCTION calc_cpv_qualif_requir()
  RETURNS VOID AS $$
BEGIN
  TRUNCATE cpv_qualif_requir;
  INSERT INTO cpv_qualif_requir
  SELECT distinct on (classification_id, year) classification_id cpv, avg(avg_c) :: DOUBLE PRECISION, year
  FROM (SELECT DISTINCT ON (classification_id, tc.tender_id) classification_id, tc.count AS avg_c, year
        FROM item i
               JOIN (SELECT tender_id, count(*), extract(year from date_published) as year
                     FROM qualification_requirement qr
                            JOIN tender t ON qr.tender_id = t.id
                     WHERE t.procurement_method_details in ('oneStage', 'downgrade', 'simplicated')
                       AND t.status = 'complete'
                       AND EXTRACT(DAYS FROM now() - t.date_published) <= 365
                       AND t.bad_quality IS FALSE
                     GROUP BY tender_id, year) AS tc ON i.tender_id = tc.tender_id) AS table1
  where classification_id is not null
  GROUP BY classification_id, year;

END;
$$
LANGUAGE 'plpgsql';