package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.Ignore;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
@Ignore
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Override
    public void createWithException() throws Exception {
        Assume.assumeTrue(false);
    }
}