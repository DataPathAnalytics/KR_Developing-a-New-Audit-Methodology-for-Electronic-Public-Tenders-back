package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.QuestionCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCategoriesResponse {

    private List<QuestionCategoryDTO> categories = new ArrayList<>();

}
