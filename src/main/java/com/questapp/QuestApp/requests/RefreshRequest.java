package com.questapp.QuestApp.requests;

import lombok.Data;

@Data
public class RefreshRequest {
    Long id;
    String refreshToken;
}
