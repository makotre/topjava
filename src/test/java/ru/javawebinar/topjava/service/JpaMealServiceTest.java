package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"postgres", "jpa"})
public class JpaMealServiceTest extends MealServiceTest{
}
