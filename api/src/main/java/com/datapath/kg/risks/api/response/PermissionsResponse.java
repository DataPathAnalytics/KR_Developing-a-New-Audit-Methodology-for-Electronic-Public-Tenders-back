package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.PermissionDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionsResponse {

    private List<PermissionDTO> permissions = new ArrayList<>();

    public void addPermission(PermissionDTO dto) {
        permissions.add(dto);
    }

}
