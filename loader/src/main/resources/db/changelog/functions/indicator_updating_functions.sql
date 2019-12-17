--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_transaction_variables()
  RETURNS VOID AS $$
BEGIN
  PERFORM calc_tender_cpv_list();
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_analytic_tables()
  RETURNS VOID AS $$
BEGIN
  PERFORM calc_active_cpv();
  PERFORM calc_report_cpv();
  PERFORM calc_report_one_time();
  PERFORM calc_cpv_by_open_tender();
  PERFORM calc_pe_supplier();
--   PERFORM calc_cpv_qualif_requir();
  PERFORM calc_pe_open_tenders();
  PERFORM calc_cpv_mean_price();
  PERFORM calc_cpv_one_supplier();
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_indicators()
  RETURNS VOID AS $$
BEGIN
  PERFORM update_krai_1();
  PERFORM update_krai_2();
  PERFORM update_krai_3();
  PERFORM update_krai_4();
  PERFORM update_krai_6();
  PERFORM update_krai_7();
  PERFORM update_krai_9();
  PERFORM update_krai_10();
  PERFORM update_krai_11();
  PERFORM update_krai_14();
  PERFORM update_krai_15();
  PERFORM update_krai_16();
  PERFORM update_krai_17();
  PERFORM update_krai_18();
  PERFORM update_krai_20();
  PERFORM update_krai_23();
  PERFORM update_krai_25();
  PERFORM update_krai_26();
  PERFORM update_krai_28();
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_prioritization()
  RETURNS VOID AS $$
BEGIN
  PERFORM calc_tender_prioritization();
  PERFORM calc_buyer_prioritization();
END;
$$
LANGUAGE 'plpgsql';