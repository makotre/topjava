package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("get all meals for user {}", userId);
        model.addAttribute("meals",
                MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request, HttpSession session) {
        int id = getId(request);
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
        log.info("delete meal {} for user {}", id, userId);
        return redirectUrl(session);
    }

    @GetMapping("/create")
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("create meal {} for user {}", meal, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
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
    public String save(HttpServletRequest request, HttpSession session) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            meal.setId(getId(request));
            log.info("save updated meal {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            log.info("save new meal {} for user {}", meal, userId);
            service.create(meal, userId);
        }

        return redirectUrl(session);
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model, HttpSession session) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        session.setAttribute("filterStartDate", startDate);
        session.setAttribute("filterEndDate", endDate);
        session.setAttribute("filterStartTime", startTime);
        session.setAttribute("filterEndTime", endTime);

        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}",
                startDate, endDate, startTime, endTime, userId);
        model.addAttribute("meals",
                MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, userId),
                        SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    public String redirectUrl(HttpSession session) {
        StringBuilder redirectUrl = new StringBuilder("redirect:/meals");

        LocalDate filterStartDate = (LocalDate) session.getAttribute("filterStartDate");
        LocalDate filterEndDate = (LocalDate) session.getAttribute("filterEndDate");
        LocalTime filterStartTime = (LocalTime) session.getAttribute("filterStartTime");
        LocalTime filterEndTime = (LocalTime) session.getAttribute("filterEndTime");

        if (filterStartDate != null || filterEndDate != null || filterStartTime != null || filterEndTime != null) {
            redirectUrl.append("?startDate=");
            redirectUrl.append(filterStartDate != null ? filterStartDate : "");
            redirectUrl.append("&endDate=");
            redirectUrl.append(filterEndDate != null ? filterEndDate : "");
            redirectUrl.append("&startTime=");
            if (filterStartTime != null) {
                redirectUrl.append(URLEncoder.encode(filterStartTime.toString(), StandardCharsets.UTF_8));
            }
            redirectUrl.append("&endTime=");
            if (filterEndTime != null) {
                redirectUrl.append(URLEncoder.encode(filterEndTime.toString(), StandardCharsets.UTF_8));
            }
        }
        return redirectUrl.toString();
    }
}
