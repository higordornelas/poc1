package com.higor.poc1.domain.enumerator;

public enum CustomerType {

    LEGAL_PERSON("Legal", "CPF", CpfGroup.class),
    JURIDICAL_PERSON("Juridical", "CNPJ", CnpjGroup.class);

    private final String description;
    private final String document;
    private final Class<?> group;

    private CustomerType(String description, String document, Class<?> group) {
        this.description = description;
        this.document = document;
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public String getDocument() {
        return document;
    }

    public Class<?> getGroup() {
        return group;
    }
}
