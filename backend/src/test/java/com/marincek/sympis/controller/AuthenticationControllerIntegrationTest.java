package com.marincek.sympis.controller;

import com.marincek.sympis.BaseDatabaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@Transactional
public class AuthenticationControllerIntegrationTest extends BaseDatabaseIntegrationTest {

    private String CONTENT_TYPE_HEADER = "Content-Type";
    private String CONTENT_TYPE_HEADER_VALUE = "application/json";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testRegistration() throws Exception {
        String json = "{\"email\": \"test@gmail.com\",\"username\": \"someusername\",\"password\": \"somepassword\",\"firstName\": \"John\",\"lastName\": \"Doe\"}";

        this.mockMvc.perform(
                post("/auth/register")
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("someusername"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testRegistrationWithOutPassword() throws Exception {
        String json = "{\"email\": \"test@gmail.com\",\"username\": \"someusername\",\"firstName\": \"John\",\"lastName\": \"Doe\"}";

        this.mockMvc.perform(
                post("/auth/register")
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Please provide password"));
    }

    @Test
    public void testRegistrationWithOutUsername() throws Exception {
        String json = "{\"email\": \"test@gmail.com\",\"password\": \"somepassword\",\"firstName\": \"John\",\"lastName\": \"Doe\"}";

        this.mockMvc.perform(
                post("/auth/register")
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Please provide username"));
    }

    @Test
    public void testRegistrationWithInvalidEmail() throws Exception {
        String json = "{\"email\": \"test.gmailcom\",\"username\": \"someusername\",\"password\": \"somepassword\",\"firstName\": \"John\",\"lastName\": \"Doe\"}";

        this.mockMvc.perform(
                post("/auth/register")
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("must be a well-formed email address"));
    }

    @Test
    public void testLoginNonExisting() throws Exception {
        String json = "{\"username\": \"randomUser\",\"password\": \"randomPassowrd\"}";

        this.mockMvc.perform(
                post("/auth/login")
                        .content(json)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Unknown User"));
    }

    @Test
    public void testRegisterAndLogin() throws Exception {
        String jsonRegister = "{\"email\": \"newUser@gmail.com\",\"username\": \"newUser\",\"password\": \"newUserPass\",\"firstName\": \"John\",\"lastName\": \"Doe\"}";
        this.mockMvc.perform(
                post("/auth/register")
                        .content(jsonRegister)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        String jsonLogin = "{\"username\": \"newUser\",\"password\": \"newUserPass\"}";
        this.mockMvc.perform(
                post("/auth/login")
                        .content(jsonLogin)
                        .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
