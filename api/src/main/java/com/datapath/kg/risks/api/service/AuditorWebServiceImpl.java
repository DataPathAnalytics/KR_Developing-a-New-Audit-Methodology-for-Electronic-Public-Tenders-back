package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.PermissionEntity;
import com.datapath.kg.risks.api.dao.repository.AuditorRepository;
import com.datapath.kg.risks.api.dao.repository.PermissionRepository;
import com.datapath.kg.risks.api.dao.service.ChecklistDAOService;
import com.datapath.kg.risks.api.dto.AuditorDTO;
import com.datapath.kg.risks.api.dto.AuditorSettingsDTO;
import com.datapath.kg.risks.api.dto.AuditorsResponse;
import com.datapath.kg.risks.api.dto.PermissionDTO;
import com.datapath.kg.risks.api.exception.AuditorRegisterException;
import com.datapath.kg.risks.api.exception.ResetPasswordException;
import com.datapath.kg.risks.api.exception.UpdatePasswordException;
import com.datapath.kg.risks.api.request.RegisterAuditorRequest;
import com.datapath.kg.risks.api.request.ResetPasswordRequest;
import com.datapath.kg.risks.api.response.PermissionsResponse;
import com.datapath.kg.risks.api.security.ConfirmationTokenStorageService;
import com.datapath.kg.risks.api.security.UsersStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.datapath.kg.risks.api.Utils.getUserEmail;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Component
public class AuditorWebServiceImpl implements AuditorWebService {

    private static final float DEFAULT_VALUE_RANK = 0.5f;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuditorRepository auditorRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private DTOEntityMapper mapper;
    @Autowired
    private ChecklistDAOService checklistDAOService;
    @Autowired
    private ConfirmationTokenStorageService tokenStorageService;
    @Autowired
    private EmailSenderService emailSender;


    @Override
    public void register(RegisterAuditorRequest request) {
        AuditorEntity auditor = auditorRepository.findByEmail(request.getEmail());

        if (nonNull(auditor)) {
            if (auditor.isAccountLocked()) {
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                auditor.setPassword(encodedPassword);
                auditor.setDisabled(true);
                auditor.setAccountLocked(false);

                auditor.setTenderValueRank(DEFAULT_VALUE_RANK);
                auditor.setTenderRiskLevelRank(DEFAULT_VALUE_RANK);
                auditor.setBuyerValueRank(DEFAULT_VALUE_RANK);
                auditor.setBuyerRiskLevelRank(DEFAULT_VALUE_RANK);

                auditorRepository.save(auditor);
            } else {
                throw new AuditorRegisterException("Auditor with this email already exist.");
            }
        } else {
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            AuditorEntity entity = new AuditorEntity();
            entity.setEmail(request.getEmail());
            entity.setPassword(encodedPassword);
            entity.setDisabled(true);

            entity.setTenderValueRank(DEFAULT_VALUE_RANK);
            entity.setTenderRiskLevelRank(DEFAULT_VALUE_RANK);
            entity.setBuyerValueRank(DEFAULT_VALUE_RANK);
            entity.setBuyerRiskLevelRank(DEFAULT_VALUE_RANK);

            auditorRepository.save(entity);
        }
    }

    @Override
    public AuditorsResponse getAuditors() {
        AuditorsResponse response = new AuditorsResponse();
        List<AuditorEntity> auditorEntities = auditorRepository.getAllActiveAuditors();
        List<AuditorDTO> auditors = auditorEntities.stream()
                .map(entity -> {
                    AuditorDTO dto = mapper.map(entity);
                    dto.setPermissions(entity.getPermissions().stream().map(PermissionEntity::getId).collect(toList()));
                    return dto;
                })
                .collect(toList());
        response.setAuditors(auditors);
        return response;
    }

    @Override
    public AuditorDTO updateAuditor(AuditorDTO dto) {
        AuditorEntity entity = auditorRepository.getOne(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setDisabled(dto.isDisabled());

        if (dto.isDisabled()) {
            UsersStorageService.removeUser(dto.getId());
        }

        List<PermissionEntity> permissions = permissionRepository.findAllById(dto.getPermissions());
        entity.setPermissions(permissions);
        return mapper.map(auditorRepository.save(entity));
    }

    @Transactional
    @Override
    public void deleteAuditor(Integer id) {
        checklistDAOService.getActiveChecklistsByAuditor(id)
                .forEach(checklistId -> checklistDAOService.deleteChecklist(checklistId));

        UsersStorageService.removeUser(id);

        if (checklistDAOService.getCompletedChecklistsCount(id) > 0) {
            AuditorEntity auditor = auditorRepository.getOne(id);
            auditor.setAccountLocked(true);
            auditorRepository.save(auditor);
        } else {
            auditorRepository.deleteById(id);
        }
    }

    @Override
    public PermissionsResponse getAllPermissions() {
        PermissionsResponse response = new PermissionsResponse();
        List<PermissionDTO> permissions = permissionRepository.findAll().stream()
                .map(entity -> mapper.map(entity))
                .collect(toList());
        response.setPermissions(permissions);
        return response;
    }

    @Override
    public AuditorSettingsDTO getAuditorSettings() {
        return mapper.mapAuditorSettings(auditorRepository.findByEmail(getUserEmail()));
    }

    @Override
    public void updateAuditorSettings(AuditorSettingsDTO request) {
        AuditorEntity auditor = auditorRepository.findByEmail(getUserEmail());
        auditor.setTenderValueRank(request.getTenderValueRank());
        auditor.setTenderRiskLevelRank(request.getTenderRiskLevelRank());
        auditor.setBuyerValueRank(request.getBuyerValueRank());
        auditor.setBuyerRiskLevelRank(request.getBuyerRiskLevelRank());
        auditorRepository.save(auditor);
    }

    @Override
    public void updatePassword(RegisterAuditorRequest request) {
        AuditorEntity auditor = auditorRepository.findByEmail(request.getEmail());

        if (isNull(auditor)) throw new UpdatePasswordException("Not found user with email: " + request.getEmail());

        String encodedNewPassword = passwordEncoder.encode(request.getPassword());

        auditor.setPassword(encodedNewPassword);

        auditorRepository.save(auditor);

        UsersStorageService.removeUser(auditor.getId());
    }

    @Override
    public void sendMailForResetPassword(String email) {
        AuditorEntity auditor = auditorRepository.findByEmail(email);

        if (isNull(auditor)) throw new ResetPasswordException("Not found user with email: " + email, "RP3");

        String token = UUID.randomUUID().toString();

        emailSender.send(email, token);
        tokenStorageService.add(auditor.getEmail(), token);
    }

    @Override
    public void checkTokenForResetPassword(String token) {
        if (!tokenStorageService.isPresent(token)) throw new ResetPasswordException("Confirmation token is expired.", "RP2");
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (tokenStorageService.isPresent(request.getToken())) {
            String email = tokenStorageService.getEmail(request.getToken());
            AuditorEntity auditor = auditorRepository.findByEmail(email);

            if (nonNull(auditor)) {
                String encodedNewPassword = passwordEncoder.encode(request.getPassword());
                auditor.setPassword(encodedNewPassword);
                auditorRepository.save(auditor);
                tokenStorageService.removed(email);
                UsersStorageService.removeUser(auditor.getId());
            } else {
                throw new ResetPasswordException("Not found user with email: " + email, "RP3");
            }

        } else {
            throw new ResetPasswordException("Confirmation token is expired.", "RP2");
        }
    }
}