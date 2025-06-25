package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int ID1 = START_SEQ + 3;
    public static final int ID2 = START_SEQ + 4;
    public static final int ID3 = START_SEQ + 5;
    public static final int ID4 = START_SEQ + 6;


    public final static Meal meal1 = new Meal(ID1, LocalDateTime.of(2025, 6, 24, 11, 0), "Завтрак для юзера", 350);
    public final static Meal meal2 = new Meal(ID2, LocalDateTime.of(2025, 6, 24, 11, 0), "Завтрак для админа", 350);
    public final static Meal meal3 = new Meal(ID3, LocalDateTime.of(2025, 6, 24, 13, 0), "Обед: курица с рисом", 600);
    public final static Meal meal4 = new Meal(ID4, LocalDateTime.of(2025, 6, 24, 19, 0), "Ужин: овощной салат", 400);

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
}
