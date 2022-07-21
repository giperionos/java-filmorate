package ru.yandex.practicum.filmorate.storage.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.sql.PreparedStatement;
import java.util.Collection;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Event> getByUserId(Long id) {
        final String sqlQuery =
                "select\n" +
                "    E.*,\n" +
                "    EVENTTYPE_NAME,\n" +
                "    OPERATION_NAME\n" +
                "from EVENT E\n" +
                "left join EVENTTYPE T on E.EVENTTYPE_ID = T.EVENTTYPE_ID\n" +
                "left join OPERATION O on O.OPERATION_ID = E.OPERATION_ID\n" +
                "where USER_ID = ?\n" +
                "order by E.EVENT_TIMESTAMP";

        final RowMapper<Event> mapper =(rs, rn) -> Event.of(
                rs.getLong("EVENT_ID"),
                rs.getTimestamp("EVENT_TIMESTAMP").getTime(),
                rs.getLong("ENTITY_ID"),
                id,
                EventType.of(
                        rs.getInt("EVENTTYPE_ID"),
                        rs.getString("EVENTTYPE_NAME")
                ),
                Operation.of(
                        rs.getInt("OPERATION_ID"),
                        rs.getString("OPERATION_NAME")
                )
        );
        return jdbcTemplate.query(sqlQuery, mapper, id);
    }

    @Override
    public void add(Event event) {
        final String sqlQuery =
                "insert into EVENT(ENTITY_ID, USER_ID, EVENTTYPE_ID, OPERATION_ID) " +
                "values (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlQuery,
                event.getEntityId(),
                event.getUserId(),
                event.getEventType().getId(),
                event.getOperation().getId());
    }
}
