package com.datapath.kg.risks.api.service;

public interface EmailSenderService {
    void send(String email, String token);
}
