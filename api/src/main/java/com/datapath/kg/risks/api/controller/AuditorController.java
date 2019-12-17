package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.AuditorDTO;
import com.datapath.kg.risks.api.dto.AuditorSettingsDTO;
import com.datapath.kg.risks.api.dto.AuditorsResponse;
import com.datapath.kg.risks.api.request.RegisterAuditorRequest;
import com.datapath.kg.risks.api.request.ResetPasswordRequest;
import com.datapath.kg.risks.api.response.PermissionsResponse;
import com.datapath.kg.risks.api.service.AuditorWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auditors")
public class AuditorController {

    @Autowired
    private AuditorWebService service;

    @PostMapping("register")
    public void register(@Valid @RequestBody RegisterAuditorRequest auditor) {
        service.register(auditor);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin.base')")
    public AuditorsResponse getAuditors() {
        return service.getAuditors();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin.base')")
    public AuditorDTO updateAuditor(@Valid @RequestBody AuditorDTO auditor) {
        return service.updateAuditor(auditor);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin.base')")
    public void deleteAuditor(@PathVariable Integer id) {
        service.deleteAuditor(id);
    }

    @GetMapping("permissions")
    public PermissionsResponse getAllPermissions() {
        return service.getAllPermissions();
    }

    @GetMapping("settings")
    public AuditorSettingsDTO getAuditorSettings() {
        return service.getAuditorSettings();
    }

    @PutMapping("settings")
    public void updateAuditorSettings(@RequestBody AuditorSettingsDTO request) {
        service.updateAuditorSettings(request);
    }

    @PostMapping("update-password")
    @PreAuthorize("hasAnyAuthority('admin.base')")
    public void updatePassword(@Valid @RequestBody RegisterAuditorRequest request) {
        service.updatePassword(request);
    }

    @GetMapping("password/reset/mail")
    public void sendMailResetPassword(@RequestParam String email) {
        service.sendMailForResetPassword(email);
    }

    @GetMapping("password/reset/check")
    public void checkResetPassword(@RequestParam String token) {
        service.checkTokenForResetPassword(token);
    }

    @PostMapping("password/reset/save")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);
    }
}