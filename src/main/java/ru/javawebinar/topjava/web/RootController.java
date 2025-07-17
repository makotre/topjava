package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService service;

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        log.info("users");
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        log.info("setUser {}", userId);
        SecurityUtil.setAuthUserId(userId);
        return "redirect:meals";
    }
}

//<spring:message code="app.title"/>
//<spring:message code="${param.action == 'create' ? 'meal.create' : 'meal.edit'}"/>
//<h3><a href="${pageContext.request.contextPath}"><spring:message code="app.home"/></a></h3>
//
//<a href="meals"><spring:message code="app.title"/></a>
//
//        <form method="post" action="users">
//        <spring:message code="app.login"/>: <select name="userId">
//        <option value="100000" selected>User</option>
//        <option value="100001">Admin</option>
//    </select>
//        <button type="submit"><spring:message code="common.select"/></button>
//        </form>
