package com.datapath.kg.risks.api.comparators;

import com.datapath.kg.risks.api.dto.QuestionDTO;

import java.util.Comparator;

public class QuestionComparator implements Comparator<QuestionDTO> {
    @Override
    public int compare(QuestionDTO o1, QuestionDTO o2) {
        String[] o1parts = o1.getNumber().split("\\.");
        String[] o2Parts = o2.getNumber().split("\\.");

        int o1Integer = Integer.parseInt(o1parts[0]);
        int o2Integer = Integer.parseInt(o2Parts[0]);

        if (o1Integer != o2Integer) return Integer.compare(o1Integer, o2Integer);

        int o1Fractional = o1parts.length == 1 ? 0 : Integer.parseInt(o1parts[1]);
        int o2Fractional = o2Parts.length == 1 ? 0 :Integer.parseInt(o2Parts[1]);

        return Integer.compare(o1Fractional, o2Fractional);
    }
}
