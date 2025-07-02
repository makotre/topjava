package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
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

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static final List<String> res = new ArrayList<>();

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.info(String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
            String result = String.format("%s: %d microseconds",
                    description.getMethodName(), TimeUnit.NANOSECONDS.toMicros(nanos));
            res.add(result);
        }
    };

    @AfterClass
    public static void print() {
        log.info("\n----------------------");
        for(String str : res) {
            log.info(str);
        }
        log.info("\n----------------------");
    }


//    private static String watchedLog = "\n----------------------\n";
//
//    @Rule
//    public final Stopwatch watchman = new TestRule() {
//        @Override
//        public Statement apply(Statement statement, Description description) {
//            return null;
//        }
//
//        private long startTime;
//
//        @Override
//        protected void succeeded(Description description) {
//            watchedLog += description.getMethodName() + " succeeded" +
//                    " in " + (System.nanoTime() - startTime) + "ms\n";
//        }
//
//        @Override
//        protected void failed(Throwable e, Description description) {
//            watchedLog += e.getClass().getSimpleName() + " " + description.getMethodName() + " failed" +
//                    " in " + (System.nanoTime() - startTime) + "ms\n";
//        }
//
//        @Override
//        protected void skipped(AssumptionViolatedException e, Description description) {
//            watchedLog += e.getClass().getSimpleName() + description.getMethodName() + " skipped" +
//                    " in " + (System.nanoTime() - startTime) + "ms\n";
//        }
//
//        @Override
//        protected void starting(Description description) {
//            startTime = System.nanoTime();
//        }
//
//        @Override
//        protected void finished(Description description) {
//            watchedLog += description.getMethodName() + " in " + (System.nanoTime() - startTime) + "ms\n";
//        }
//    };
//

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void getAll() {
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void getBetweenInclusive() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), meals);
    }
}