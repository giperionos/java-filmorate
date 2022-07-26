package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
public class UserFriend {

    private Long ownerUserId;
    private Long friendId;
    private String status;
}
