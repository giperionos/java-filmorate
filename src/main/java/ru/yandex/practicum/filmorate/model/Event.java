package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "of")
public class Event {

    private Long eventId;
    private Long timestamp;
    private final Long entityId;
    private final Long userId;
    @JsonIgnore
    private final EventType eventType;
    @JsonIgnore
    private final Operation operation;

    @JsonAnyGetter
    public Map<String, Object> getEvent() {
        return Map.of(
                "eventType", eventType.getName(),
                "operation", operation.getName()
        );
    }
}
