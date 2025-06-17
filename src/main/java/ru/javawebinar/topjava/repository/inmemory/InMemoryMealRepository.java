package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(SecurityUtil.authUserId());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        // only if updated meal belong to userId, which is now there is only one authUserId
        if (meal.getUserId() != SecurityUtil.authUserId()) return null;
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        if (mealsMap.get(id).getUserId() != SecurityUtil.authUserId()) return false;
        return mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        if (mealsMap.get(id).getUserId() != SecurityUtil.authUserId()) return null;
        return mealsMap.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId() == SecurityUtil.authUserId())
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<MealTo> getBetween(LocalDateTime startDate, LocalTime startTime, LocalDateTime endDate, LocalTime endTime) {
        return MealsUtil.getFilteredTos(getAll().stream()
                        .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate))
                        .collect(Collectors.toList()),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime, endTime);
    }
}

