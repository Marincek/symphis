package com.marincek.sympis.repository;

import com.marincek.sympis.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp(){
        User user = new User("therock");
        user.setPassword("passcode");
        user.setEmail("test@test.com");
        user.setFirstName("Dwayne");
        user.setLastName("Johnson");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindByUsernameThenReturnUser() {
        // given
        String username = "therock";

        // when
        Optional<User> found = userRepository.findByUsername(username);

        // then
        assertTrue(found.isPresent());
        assertEquals(username, found.get().getUsername());
    }

    @Test
    public void whenFindFirstNameThenReturnString() {
        // given
        String username = "therock";

        // when
        Optional<String> found = userRepository.findFirstName(username);

        // then
        assertTrue(found.isPresent());
        assertEquals("Dwayne", found.get());
    }
}
