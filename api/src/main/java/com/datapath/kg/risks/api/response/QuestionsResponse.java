package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.QuestionDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionsResponse {

    private List<QuestionDTO> questions = new ArrayList<>();

}