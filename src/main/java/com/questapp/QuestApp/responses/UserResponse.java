package com.questapp.QuestApp.responses;

import com.questapp.QuestApp.entities.User;
import lombok.Data;

@Data
public class UserResponse {

    Long id;
    int avatarId;
    String name;

    public UserResponse(User user) {
        this.id = user.getId();
        this.avatarId = user.getAvatar();
        this.name = user.getUserName();
    }
}
