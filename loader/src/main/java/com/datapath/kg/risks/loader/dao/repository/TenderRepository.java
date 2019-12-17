package com.datapath.kg.risks.loader.dao.repository;

import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenderRepository extends JpaRepository<TenderEntity, Integer> {

    String KRAI5_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 5\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND status = 'complete'\n" +
            "  AND procurement_method_details = 'singleSource'\n" +
            "  AND procurement_method_rationale = 'additionalProcurement10'";

    String KRAI8_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 8\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND status = 'complete'\n" +
            "  AND procurement_method_details = 'singleSource'\n" +
            "  AND procurement_method_rationale = 'additionalProcurement25'";

    String KRAI12_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "            LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 12\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details = 'oneStage'\n" +
            "  AND\n" +
            "       (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI13_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 13\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details = 'simplicated'\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI19_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 19\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI21_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 21\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI22_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 22\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI29_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 29\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI30_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 30\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI31_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 31\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    String KRAI32_ACCEPTABLE_TENDERS = "SELECT t.id\n" +
            "FROM tender t\n" +
            "       LEFT JOIN tender_indicator i on t.id = i.tender_id AND i.indicator_id = 32\n" +
            "WHERE i.tender_id IS NULL AND t.date_published >= '2018-01-01'\n" +
            "  AND procurement_method_details IN('simplicated','oneStage','downgrade')\n" +
            "  AND\n" +
            "  (status = 'complete' OR (status = 'active' AND current_stage = 'evaluationComplete'))";

    @Query(value = "select id from tender where extract(years from date_published) >= extract(years from now()) - 1", nativeQuery = true)
    List<Integer> findAllIds();

    @Query(value = KRAI5_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI5AcceptableTenders();

    @Query(value = KRAI8_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI8AcceptableTenders();

    @Query(value = KRAI12_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI12AcceptableTenders();

    @Query(value = KRAI13_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI13AcceptableTenders();

    @Query(value = KRAI19_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI19AcceptableTenders();

    @Query(value = KRAI21_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI21AcceptableTenders();

    @Query(value = KRAI22_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI22AcceptableTenders();

    @Query(value = KRAI29_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI29AcceptableTenders();

    @Query(value = KRAI30_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI30AcceptableTenders();

    @Query(value = KRAI31_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI31AcceptableTenders();

    @Query(value = KRAI32_ACCEPTABLE_TENDERS, nativeQuery = true)
    List<Integer> getKRAI32AcceptableTenders();
}
