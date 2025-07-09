package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"postgres", "jdbc"})
public class JdbcUserServiceTest extends UserServiceTest{
}
