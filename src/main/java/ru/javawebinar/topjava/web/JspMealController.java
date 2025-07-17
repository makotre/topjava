package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping
    public String getMeals(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("get all meals for user {}", userId);
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
        log.info("delete meal {} for user {}", id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("create meal {} for user {}", meal, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal = service.get(Integer.parseInt(Objects.requireNonNull(request.getParameter("id"))), userId);
        log.info("update meal {} for user {}", meal, userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(id)) {
            meal.setId(Integer.parseInt(Objects.requireNonNull(id)));
            log.info("save updated meal {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            log.info("save new meal {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, userId),
                        SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
