package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID2, USER_ID));
    }

    @Test
    public void delete() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID1, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(),
                        "duplicateDateTime", 250), USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        // Admin обновляет meal1 для user
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> all = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(all, meal4, meal3, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, meal4, meal3, meal1);
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer new_user_meal_ID = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(new_user_meal_ID);
        assertMatch(created, newMeal);
        assertMatch(service.get(new_user_meal_ID, USER_ID), newMeal);
    }
}