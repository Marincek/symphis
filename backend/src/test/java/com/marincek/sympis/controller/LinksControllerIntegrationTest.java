package com.marincek.sympis.controller;

import com.marincek.sympis.BaseDatabaseIntegrationTest;
import com.marincek.sympis.domain.AuthorizationToken;
import com.marincek.sympis.service.TokenService;
import com.marincek.sympis.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jan Marinchek
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
public class LinksControllerIntegrationTest extends BaseDatabaseIntegrationTest {

    private String BASE_LINKS_URL = "/space/links";
    private String TOKEN_TESTING = "some_generated_token";

    private String AUTH_HEADER = "x-auth-token";
    private String CONTENT_TYPE_HEADER = "Content-Type";
    private String CONTENT_TYPE_HEADER_VALUE = "application/json";

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private GenericFilterBean springSecurityFilterChain;

    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;
    @MockBean
    private UserService userService;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();

        Mockito.when(tokenService.findByToken(TOKEN_TESTING)).thenReturn(Optional.of(new AuthorizationToken("marincek",TOKEN_TESTING,new Date())));
        Mockito.when(tokenService.findByUsername("marincek")).thenReturn(Optional.of(new AuthorizationToken("marincek",TOKEN_TESTING,new Date())));

        User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername("marincek");
        builder.password("changeit");
        builder.roles("USER");
        Mockito.when(userService.loadUserByUsername("marincek")).thenReturn(builder.build());
    }

    @Test
    public void testGetLinksForUser() throws Exception {
        this.mockMvc.perform(
                get(BASE_LINKS_URL)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].url").isNotEmpty())
                .andExpect(jsonPath("$.[0].tags").isArray());
    }

    @Test
    public void testGetLinksForUnknownUser() throws Exception {
        this.mockMvc.perform(
                get(BASE_LINKS_URL)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAddLink() throws Exception {
        String json = "{\"url\": \"http://someurl.com\",\"tags\": [\"tag1\",\"tag2\", \"tag3\",\"tag4\", \"tag5\",\"tag6\", \"tag7\"]}";
        this.mockMvc.perform(
                post(BASE_LINKS_URL)
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.url").value("http://someurl.com"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.[0]").value("tag1"));
    }

    @Test
    public void testAddLinkWithoutUrl() throws Exception {
        String json = "{\"tags\": [\"tag1\",\"tag2\", \"tag3\",\"tag4\", \"tag5\",\"tag6\", \"tag7\"]}";
        this.mockMvc.perform(
                post(BASE_LINKS_URL)
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Please provide url"));
    }

    @Test
    public void testAddLinkWithoutTags() throws Exception {
        String json = "{\"url\": \"http://someurl.com\"}";
        this.mockMvc.perform(
                post(BASE_LINKS_URL)
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Please provide tags"));
    }

    @Test
    public void testAddLinkWith0Tags() throws Exception {
        String json = "{\"url\": \"http://someurl.com\",\"tags\": []}";
        this.mockMvc.perform(
                post(BASE_LINKS_URL)
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Please provide tags"));
    }

    @Test
    public void testAddLinkWithHtmlTags() throws Exception {
        String json = "{\"url\": \"http://someurl.com\",\"tags\": [\"tag1\",\"<somehtmltag>\", \"tag3\",\"tag4\", \"tag5\",\"tag6\", \"tag7\"]}";
        this.mockMvc.perform(
                post(BASE_LINKS_URL)
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("No HTML tags allowed"));
    }

    @Test
    public void testGetTags() throws Exception {
        this.mockMvc.perform(
                get(BASE_LINKS_URL+"/tags?url=https://dzone.com/articles/spring-boot-rest-service-1")
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testGetTagsNonExistingUrl() throws Exception {
        this.mockMvc.perform(
                get(BASE_LINKS_URL+"/tags?url=https://somerandomurlasdasdaadsasdasd.com")
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testDeleteLink() throws Exception {
        this.mockMvc.perform(
                delete(BASE_LINKS_URL+"/301")
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNonExistingLink() throws Exception {
        this.mockMvc.perform(
                delete(BASE_LINKS_URL+"/6486")
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE)
                        .header(AUTH_HEADER, TOKEN_TESTING))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
