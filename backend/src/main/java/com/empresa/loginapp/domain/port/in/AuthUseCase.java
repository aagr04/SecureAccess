package com.empresa.loginapp.domain.port.in;

import com.empresa.loginapp.shared.dto.request.*;
import com.empresa.loginapp.shared.dto.response.AuthResponse;

public interface AuthUseCase {

    AuthResponse login(LoginRequest request);

    void logout(String username);

    String recover(RecoverRequest request);
}
