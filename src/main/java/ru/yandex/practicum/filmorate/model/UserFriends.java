package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserFriends {
    private Long ownerUserId;
    private Long friendId;
    private String status;

    public UserFriends(Long ownerUserId, Long friendId, String status) {
        this.ownerUserId = ownerUserId;
        this.friendId = friendId;
        this.status = status;
    }
}
