package com.empresa.loginapp.domain.service;

import java.text.Normalizer;
import java.util.Set;

public class EmailGeneratorService {
    private static final String DOMAIN = "@mail.com";

    public String generate(String nombres, String apellidos, Set<String> existingEmails) {
        String cleanNombres = clean(nombres);
        if (cleanNombres.isBlank()) {
            throw new IllegalArgumentException("Nombres requeridos para generar email");
        }
        String firstInitial = cleanNombres.split("\\s+")[0].substring(0, 1);
        String lastNames = apellidoKey(apellidos);
        if (lastNames.isBlank()) {
            throw new IllegalArgumentException("Apellidos requeridos para generar email");
        }
        String base = firstInitial + lastNames;
        String email = base + DOMAIN;
        int counter = 1;
        Set<String> normalizedExistingEmails = existingEmails == null ? Set.of() :
                existingEmails.stream().filter(e -> e != null).map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
        while (normalizedExistingEmails.contains(email)) {
            email = base + counter + DOMAIN;
            counter++;
        }
        return email;
    }

    private String clean(String value) {
        String normalized = Normalizer.normalize(value == null ? "" : value.trim().toLowerCase(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").replaceAll("[^a-z ]", "").trim().replaceAll("\\s+", " ");
    }

    private String apellidoKey(String apellidos) {
        String cleanApellidos = clean(apellidos);
        if (cleanApellidos.isBlank()) return "";
        String[] parts = cleanApellidos.split("\\s+");
        StringBuilder key = new StringBuilder(parts[0]);
        if (parts.length > 1 && !parts[1].isBlank()) {
            key.append(parts[1].charAt(0));
        }
        return key.toString();
    }
}
