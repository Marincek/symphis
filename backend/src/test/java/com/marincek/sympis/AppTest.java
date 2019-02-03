package com.marincek.sympis;

import com.marincek.sympis.repository.LinkRepositoryIntegrationTest;
import com.marincek.sympis.repository.TokenRepositoryIntegrationTest;
import com.marincek.sympis.repository.UserRepositoryIntegrationTest;
import com.marincek.sympis.service.LinkServiceTest;
import com.marincek.sympis.service.TokenServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //unit
        UrlNormalizerTest.class,
        // integration repo
        LinkRepositoryIntegrationTest.class,
        UserRepositoryIntegrationTest.class,
        TokenRepositoryIntegrationTest.class,
        // unit services
        LinkServiceTest.class,
        TokenServiceTest.class
})
public class AppTest {
}
