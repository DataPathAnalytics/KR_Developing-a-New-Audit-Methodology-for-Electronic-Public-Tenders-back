--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION calc_tender_prioritization()
  RETURNS VOID AS $$
BEGIN

  TRUNCATE tender_prioritization;

  WITH tenders AS (SELECT t.id,
                          t.status,
                          t.date,
                          t.procurement_method_details,
                          (SELECT COALESCE(SUM(award.value_amount), 0)
                           FROM award
                                  JOIN lot ON lot.id = award.lot_id
                           WHERE lot.tender_id = t.id
                             AND lot.status in ('active', 'complete')
                             AND award.status = 'active') :: bigint fact_value,
                          (SELECT COALESCE(SUM(i.risk_level), 0)
                           FROM tender_indicator ti
                                  JOIN indicator i ON ti.indicator_id = i.id
                           WHERE ti.tender_id = t.id
                             AND ti.indicator_value > 0)            risk_level
                   FROM tender t
                   WHERE t.bad_quality IS FALSE
                     AND t.id <> 103981488
                     AND t.date_published > '2018-01-01'
                     AND t.procurement_method_details IN ('oneStage', 'simplicated', 'downgrade', 'singleSource')
                     AND (t.status = 'complete'
                            OR (t.status = 'active' AND
                                t.current_stage = 'evaluationComplete' AND
                                (SELECT (EXTRACT(DAYS FROM now() - date) > 30)
                                 FROM award
                                 WHERE tender_id = t.id
                                 ORDER BY date
                                 LIMIT 1)
                              )
                             )),
       tenders_prioritization AS (SELECT t.id,
                                         t.fact_value                                                                  winner_bids_value,
                                         (SELECT COUNT(id)
                                          FROM contract
                                          WHERE tender_id = t.id)                                                      contracts_count,
                                         t.fact_value                                                                  completed_lot_value,
                                         (SELECT COUNT(*)
                                          FROM lot
                                                 JOIN award ON lot.id = award.lot_id
                                          WHERE lot.tender_id = t.id
                                            AND lot.status in ('active', 'complete')
                                            AND award.status = 'active')                                               completed_lots_count,
                                         (SELECT COUNT(*) FROM lot WHERE lot.tender_id = t.id
                                                                     AND lot.status in ('active',
                                                                                        'complete'))                   lots_count,
                                         (SELECT COUNT(*) FROM bid WHERE tender_id = t.id
                                                                     AND status = 'valid')                             tenderers_count,
                                         (SELECT COUNT(*) FROM award WHERE tender_id = t.id
                                                                       AND status = 'disqualified')                    disqualifieds_count,
                                         (SELECT COUNT(*)
                                          FROM tender_supplier
                                          WHERE tender_id = t.id)                                                      suppliers_count,
                                         (SELECT COUNT(*) FROM tender_indicator WHERE tender_id = t.id
                                                                                  AND indicator_value > 0)             passed_indicators_count,
                                         (SELECT COUNT(*)
                                          FROM tender_cpv_list
                                          WHERE tender_id = t.id)                                                      cpv_count,
                                         (SELECT level
                                          FROM tender_risk_level_range
                                          WHERE procurement_method = t.procurement_method_details
                                            AND risk_level
                                                    BETWEEN left_bound AND right_bound)                                risk_level,
                                         (SELECT COALESCE(SUM(value_amount), 0)
                                          FROM contract
                                          WHERE tender_id = t.id) :: bigint                                            contract_amount,
                                         (SELECT array_agg(DISTINCT SUBSTRING(cpv, 1, 2))
                                          FROM tender_cpv_list
                                          WHERE tender_id = t.id)                                                      cpv_list,
                                         (SELECT array_agg(indicator_id) FROM tender_indicator WHERE indicator_value = 1
                                                                                                 AND tender_id = t.id) passed_indicator_list,
                                         (SELECT count(id)
                                          FROM complaint
                                          WHERE tender_id = t.id)                                                      complaints_count,
                                         (CASE
                                            WHEN t.status = 'complete' THEN t.date :: date
                                            ELSE (SELECT (date + interval '30 day') :: date
                                                  FROM award
                                                  WHERE tender_id = t.id
                                                  ORDER BY date
                                                  LIMIT 1) END)                                                        contract_date,
                                         (SELECT array_agg(distinct date_signed :: date) FROM contract c WHERE c.tender_id = t.id
                                                                                          AND c.status = 'active')     contract_signing_date
                                  FROM tenders t)
  INSERT INTO tender_prioritization
  SELECT tp.id,
         t.amount :: bigint                                                                              procedure_expected_value,
         tp.completed_lot_value,
         t.procurement_method_details,
         t.date_published :: date,
         tp.contract_date,
         tp.completed_lots_count,
         tp.tenderers_count,
         tp.disqualifieds_count,
         tp.suppliers_count,
         tp.passed_indicators_count,
         tp.cpv_count,
         tp.risk_level,
         t.buyer_id,
         t.current_stage,
         tp.contract_amount,
         tp.cpv_list,
         tp.passed_indicator_list,
         tp.contracts_count,
         tp.winner_bids_value,
         t.guarantee_amount :: bigint,
         p.identifier_id,
         p.identifier_legal_name_ru,
         t.currency                                                                                      procedure_currency,
         (SELECT description
          FROM tender_risk_level_range
          WHERE level = tp.risk_level
            AND procurement_method = t.procurement_method_details)                                       range_desc,
         tp.complaints_count,
         (SELECT array_to_string(code.c, ', ')
          FROM (SELECT array_agg(original_code) c FROM okgz WHERE code IN
                                                                  (SELECT unnest(tp.cpv_list))) AS code) okgz_list,
         (SELECT array_to_string(indicators.n, ', ')
          FROM (SELECT array_agg(name) n
                FROM indicator
                WHERE id in (SELECT unnest(tp.passed_indicator_list))) AS indicators)                    passed_indicator_name_list,
         p.region                                                                                        region,
         (SELECT array_to_string(name.n, e'\n')
          FROM (SELECT array_agg(name) n FROM okgz WHERE code IN (SELECT unnest(tp.cpv_list))) AS name)  okgz_name_list,
         tp.lots_count,
         (CASE
            WHEN tp.contract_signing_date is NULL THEN tp.contract_date :: text
            ELSE array_to_string(tp.contract_signing_date, ', ') END)                                    contract_signing_date
  FROM tenders_prioritization tp
         join tender t on tp.id = t.id
         join party p on t.buyer_id = p.id;

END;
$$
LANGUAGE 'plpgsql';