import { axiosClient } from '../api/axiosClient';
import { endpoints } from '../api/endpoints';
import type { LoginRequest, LoginResponse, RecoverPasswordRequest, RecoverPasswordResponse } from '../types/auth.types';

export const authService = {
  login: async (request: LoginRequest): Promise<LoginResponse> => {
    const { data } = await axiosClient.post<LoginResponse>(endpoints.auth.login, request);
    return data;
  },
  logout: async (): Promise<void> => {
    await axiosClient.post(endpoints.auth.logout);
  },
  recoverPassword: async (request: RecoverPasswordRequest): Promise<RecoverPasswordResponse> => {
    const { data } = await axiosClient.post<RecoverPasswordResponse>(endpoints.auth.recover, request);
    return data;
  }
};
