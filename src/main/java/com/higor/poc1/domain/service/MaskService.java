package com.higor.poc1.domain.service;

import org.springframework.stereotype.Service;

@Service
public class MaskService {

    String rgx = null;
    String maskedEmail = null;
    String maskedCPF = null;
    String maskedCNPJ = null;

    public String maskEmail (String email) {

        rgx = "(?<=.{3}).(?=[^@]*?@)";
        maskedEmail = email.replaceAll(rgx, "*");

        return maskedEmail;
    }

    public String maskCPF (String CPF) {
        rgx = "([0-9]{3}).[0-9]{3}.[0-9]{3}";
        maskedCPF = CPF.replaceAll(rgx, "$1.***.***");

        return maskedCPF;
    }

    public String maskCNPJ (String CNPJ) {

        rgx = "([0-9]{2}).[0-9]{3}/[0-9]{4}";
        maskedCNPJ = CNPJ.replaceAll(rgx, "$1.***/****");

        return maskedCNPJ;
    }
}
