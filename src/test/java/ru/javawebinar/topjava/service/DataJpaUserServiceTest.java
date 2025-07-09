package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"postgres", "datajpa"})
public class DataJpaUserServiceTest extends UserServiceTest{
}
