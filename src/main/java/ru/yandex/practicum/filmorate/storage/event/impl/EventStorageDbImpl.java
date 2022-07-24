package ru.yandex.practicum.filmorate.storage.event.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.Collection;

@Repository
public class EventStorageDbImpl implements EventStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public EventStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Event> getByEventsByUserId(Long userId) {
        final String sqlQuery =
                "select\n" +
                "    E.*\n" +
                "from EVENT E\n" +
                "where USER_ID = ?\n" +
                "order by E.EVENT_TIMESTAMP";

        final RowMapper<Event> mapper =(rs, rn) -> new Event(
                rs.getLong("EVENT_ID"),
                rs.getTimestamp("EVENT_TIMESTAMP").getTime(),
                rs.getLong("ENTITY_ID"),
                userId,
                EventType.valueOf(rs.getString("EVENT_TYPE")),
                Operation.valueOf(rs.getString("OPERATION"))
        );
        return jdbcTemplate.query(sqlQuery, mapper, userId);
    }

    @Override
    public void addNewEvent(Event event) {
        final String sqlQuery =
                "insert into EVENT(ENTITY_ID, USER_ID, EVENT_TYPE, OPERATION) " +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                event.getEntityId(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString());
    }
}
