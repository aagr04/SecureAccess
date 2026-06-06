package com.empresa.loginapp.shared.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadRequest {
    private String filename;
    private byte[] content;
}
