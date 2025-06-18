package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public int userId = SecurityUtil.authUserId();

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getFilteredTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay(),
                LocalTime.MIN, LocalTime.MAX);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkIsNew(meal);
        return service.create(meal, userId);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public List<MealTo> getBetween(LocalDateTime startDate, LocalTime startTime, LocalDateTime endDate, LocalTime endTime) {
        log.info("getBetween days - {} {}, time - {} {}", startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getBetween(startDate, endDate, userId),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}