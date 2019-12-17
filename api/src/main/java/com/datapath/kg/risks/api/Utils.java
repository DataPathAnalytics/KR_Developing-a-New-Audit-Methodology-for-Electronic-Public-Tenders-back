package com.datapath.kg.risks.api;

import com.datapath.kg.risks.api.security.AuditorAuthInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    private Utils() {
    }

    public static String convertCase(String value) {
        return value.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    public static String getUserEmail() {
        return ((AuditorAuthInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
    }

    public static String getAuditorId() {
        return ((AuditorAuthInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
