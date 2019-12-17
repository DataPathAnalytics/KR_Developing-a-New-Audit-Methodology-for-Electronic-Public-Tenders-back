package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.AuditorDTO;
import com.datapath.kg.risks.api.dto.AuditorSettingsDTO;
import com.datapath.kg.risks.api.dto.AuditorsResponse;
import com.datapath.kg.risks.api.request.RegisterAuditorRequest;
import com.datapath.kg.risks.api.request.ResetPasswordRequest;
import com.datapath.kg.risks.api.response.PermissionsResponse;

public interface AuditorWebService {

    void register(RegisterAuditorRequest request);

    AuditorsResponse getAuditors();

    AuditorDTO updateAuditor(AuditorDTO auditor);

    void deleteAuditor(Integer id);

    PermissionsResponse getAllPermissions();

    AuditorSettingsDTO getAuditorSettings();

    void updateAuditorSettings(AuditorSettingsDTO request);

    void updatePassword(RegisterAuditorRequest request);

    void sendMailForResetPassword(String email);

    void checkTokenForResetPassword(String token);

    void resetPassword(ResetPasswordRequest token);
}