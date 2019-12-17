--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true runAlways:true

CREATE OR REPLACE FUNCTION update_krai_1()
  RETURNS void AS $$
DECLARE
  _tender                RECORD;
  _tender_cpv_list       RECORD;
  _report_one_time       RECORD;

  _exist                 BOOLEAN := FALSE;

  _INDICATOR_ID CONSTANT integer := 1;
  _PASSED CONSTANT       integer := 1;
  _INCORRECT CONSTANT    integer := -1;
  _FAILED CONSTANT       integer := 0;

BEGIN
  DELETE FROM tender_indicator WHERE indicator_id = _INDICATOR_ID;

  FOR _tender IN SELECT id, date, date_published, buyer_id, bad_quality
                 FROM tender t
                 WHERE t.procurement_method_rationale = 'annualProcurement'
                   AND t.status = 'complete'
                   AND t.procurement_method_details = 'singleSource'
                   AND t.date_published >= '2018-01-01'
  LOOP
    RAISE NOTICE '%', _tender.id;
    if _tender.bad_quality IS TRUE
    then
      insert into tender_indicator values (_tender.id, _INDICATOR_ID, _INCORRECT);
    else
      FOR _tender_cpv_list IN SELECT * FROM tender_cpv_list tcl WHERE tcl.tender_id = _tender.id
      LOOP
        FOR _report_one_time IN SELECT *
                                FROM report_one_time rot
                                WHERE rot.buyer_id = _tender.buyer_id
                                  AND rot.published_year = EXTRACT(year from _tender.date_published)
                                  AND rot.date < _tender.date
        LOOP
          if _report_one_time.cpv = _tender_cpv_list.cpv and _tender.date > _report_one_time.date
          then
            _exist = TRUE;
            EXIT;
          end if;
        END LOOP;
        IF _exist is TRUE
        THEN
          EXIT;
        end if;
      END LOOP;
      IF _exist is TRUE
      THEN
        insert into tender_indicator values (_tender.id, _INDICATOR_ID, _PASSED);
      ELSE
        insert into tender_indicator values (_tender.id, _INDICATOR_ID, _FAILED);
      end if;
    end if;
    _exist = FALSE;
  END LOOP;
END;
$$
LANGUAGE plpgsql;