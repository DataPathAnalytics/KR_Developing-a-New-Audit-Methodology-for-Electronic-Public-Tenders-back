package com.datapath.kg.risks.api.comparators;

import com.datapath.kg.risks.api.dao.entity.AnswerEntity;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class AnswerEntityComparator implements Comparator<AnswerEntity> {

    @Override
    public int compare(AnswerEntity answer1, AnswerEntity answer2) {
        String[] category1Parts = answer1.getCategoryNumber().split("\\.");
        String[] category2Parts = answer2.getCategoryNumber().split("\\.");

        int category1Integer = Integer.parseInt(category1Parts[0]);
        int category2Integer = Integer.parseInt(category2Parts[0]);

        if (category1Integer != category2Integer) return Integer.compare(category1Integer, category2Integer);

        int category1Fractional = category1Parts.length == 1 ? 0 : Integer.parseInt(category1Parts[1]);
        int category2Fractional = category2Parts.length == 1 ? 0 : Integer.parseInt(category2Parts[1]);

        if (category1Fractional != category2Fractional) return Integer.compare(category1Fractional, category2Fractional);

        String[] question1Parts = answer1.getQuestionNumber().split("\\.");
        String[] question2Parts = answer2.getQuestionNumber().split("\\.");

        int question1Integer = Integer.parseInt(question1Parts[0]);
        int question2Integer = Integer.parseInt(question2Parts[0]);

        if (question1Integer != question2Integer) return Integer.compare(question1Integer, question2Integer);

        int question1Fractional = question1Parts.length == 1 ? 0 : Integer.parseInt(question1Parts[1]);
        int question2Fractional = question2Parts.length == 1 ? 0 : Integer.parseInt(question2Parts[1]);

        return Integer.compare(question1Fractional, question2Fractional);
    }

}