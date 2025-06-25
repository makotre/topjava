package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID1 = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID2 = START_SEQ + 4;
    public static final int USER_MEAL_ID3 = START_SEQ + 5;
    public static final int USER_MEAL_ID4 = START_SEQ + 6;


    public final static Meal meal1 = new Meal(USER_MEAL_ID1, LocalDateTime.of(2025, 6, 24, 11, 0), "Завтрак для юзера", 350);
    public final static Meal meal2 = new Meal(ADMIN_MEAL_ID2, LocalDateTime.of(2025, 6, 24, 11, 0), "Завтрак для админа", 350);
    public final static Meal meal3 = new Meal(USER_MEAL_ID3, LocalDateTime.of(2025, 6, 24, 13, 0), "Обед: курица с рисом", 600);
    public final static Meal meal4 = new Meal(USER_MEAL_ID4, LocalDateTime.of(2025, 6, 24, 19, 30), "Ужин: овощной салат", 400);

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.of(2025, 6, 25, 10, 0));
        updated.setDescription("updated");
        updated.setCalories(500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2025, 6, 25, 15, 0), "New meal", 200);
    }
}
