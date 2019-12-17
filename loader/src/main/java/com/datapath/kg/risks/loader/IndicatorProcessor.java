package com.datapath.kg.risks.loader;

import com.datapath.kg.risks.loader.handlers.KRAIHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class IndicatorProcessor {

    @Autowired
    private List<KRAIHandler> entityHandlers;

    public void process() {
        entityHandlers.forEach(KRAIHandler::handle);
    }
}