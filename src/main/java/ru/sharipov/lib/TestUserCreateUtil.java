package ru.sharipov.lib;

import ru.sharipov.dao.DaoService;
import ru.sharipov.dao.UserDaoServiceImpl;
import ru.sharipov.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestUserCreateUtil {

    private static final DaoService<User> userService = UserDaoServiceImpl.getInstance();

    public void createTestUsers(LocalDate starDate, LocalDate endDate, int stepDays) {

        for (LocalDate date = starDate; date.isBefore(endDate); date = date.plusDays(stepDays)) {
            User user = new User();
            user.setName("TestUser-" + date);
            user.setBirthCity("Казань");
            user.setBirthDay(date);
            user.setBirthTime(LocalTime.of(1, 0));
            user.setLivingCity("Москва");
            userService.save(user);
        }
    }
}
