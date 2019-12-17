package com.datapath.kg.risks.api.dao.specification;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.request.ChecklistRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

public class ChecklistSpecification implements Specification<ChecklistEntity> {

    private ChecklistRequest request;
    private AuditorEntity auditor;

    public ChecklistSpecification(ChecklistRequest request, AuditorEntity auditor) {
        this.auditor = auditor;
        this.request = request;
    }

    @Override
    public Predicate toPredicate(Root<ChecklistEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        boolean isAdmin = auditor.getPermissions().stream().anyMatch(perm -> perm.getName().equalsIgnoreCase("admin.base"));
        if (isAdmin) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status").get("id"), 2)));
        } else {
            if (nonNull(request.getStatus())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status").get("id"), request.getStatus())));
            }
        }

        if (nonNull(request.getTemplateType())) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("templateTypeId"), request.getTemplateType())));
        }

        if (request.isOnlyMyChecklists()) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditor").get("id"), auditor.getId())));
        }

        if (!isEmpty(request.getIds())) {
            predicates.add(criteriaBuilder.and(root.get("id").in(request.getIds())));
        }

        if (!isEmpty(request.getAuditorIds())) {
            predicates.add(criteriaBuilder.and(root.get("auditor").get("id").in(request.getAuditorIds())));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
