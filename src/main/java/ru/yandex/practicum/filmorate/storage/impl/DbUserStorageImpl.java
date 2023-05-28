package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.Mapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbUserStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<List<User>> getAllUsers() {
        String sqlQueryGetUsers = "SELECT USER_ID id,\n" +
                "       USER_NAME name,\n" +
                "       EMAIL email,\n" +
                "       LOGIN login,\n" +
                "       BIRTHDAY birthday\n" +
                "FROM USERS\n" +
                "ORDER BY USER_ID";

        List<User> users = jdbcTemplate.query(sqlQueryGetUsers, Mapper::mapRowToUser);

        return Optional.of(users);
    }

    @Override
    public Optional<User> getUser(Long id) {
        String sqlQueryGetUser = "SELECT USER_ID id,\n" +
                "       USER_NAME name,\n" +
                "       EMAIL email,\n" +
                "       LOGIN login,\n" +
                "       BIRTHDAY birthday\n" +
                "FROM USERS\n" +
                "WHERE USER_ID = ?" +
                "ORDER BY USER_ID";

        User user;

        try {
            user = jdbcTemplate.queryForObject(sqlQueryGetUser, Mapper::mapRowToUser, id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

        assert user != null;

        return Optional.of(user);
    }

    @Override
    public Optional<User> addNewUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");

        Long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();

        return getUser(userId);
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQueryUpdateUser = "UPDATE USERS\n" +
                "SET EMAIL = ?,\n" +
                "    LOGIN = ?,\n" +
                "    USER_NAME = ?,\n" +
                "    BIRTHDAY = ?\n" +
                "WHERE USER_ID = ?";

        try {
            jdbcTemplate.update(sqlQueryUpdateUser,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );
        } catch (DataAccessException e) {
            return Optional.empty();
        }

        return getUser(user.getId());
    }

    @Override
    public Optional<List<User>> getFriends(Long id) {
        String sqlQueryGetFriends = "SELECT USERS.user_id  id,\n" +
                "       email email,\n" +
                "       login login,\n" +
                "       user_name name,\n" +
                "       birthday birthday\n" +
                "FROM USERS\n" +
                "    inner join friendship f on USERS.USER_ID = f.FRIEND_ID\n" +
                "WHERE f.USER_ID = ?" +
                "ORDER BY id";

        return Optional.of(jdbcTemplate.query(sqlQueryGetFriends, Mapper::mapRowToUser, id));
    }

    @Override
    public Optional<List<User>> addFriend(Long idUser, Long idFriend) {
        String sqlQueryAddFriend = "INSERT INTO friendship (user_id, friend_id)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQueryAddFriend, idUser, idFriend);
        return getFriends(idUser);
    }

    @Override
    public Optional<List<User>> removeFriend(Long idUser, Long idFriend) {
        String sqlQueryRemoveFriend = "DELETE FROM friendship " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQueryRemoveFriend, idUser, idFriend);
        return getFriends(idUser);
    }
}
