package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Collection<Meal> getAll() {
        return repository.getAll();
    }
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public Meal get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public void update(Meal meal) {
        checkNotFound(repository.save(meal), meal.getId());
    }

    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    public List<MealTo> getBetween(LocalDateTime startDate, LocalTime startTime, LocalDateTime endDate, LocalTime endTime) {
        return repository.getBetween(startDate, startTime, endDate, endTime);
    }
}