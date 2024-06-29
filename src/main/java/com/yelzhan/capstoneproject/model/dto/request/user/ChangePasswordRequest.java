package com.yelzhan.capstoneproject.model.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String matchingPassword;

}
