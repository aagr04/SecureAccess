package com.empresa.loginapp.shared.dto.response;

import lombok.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadResponse {
    private int totalProcesados;
    private int exitosos;
    private int fallidos;
    @Builder.Default
    private List<String> erroresPorFila = new ArrayList<>();
}
