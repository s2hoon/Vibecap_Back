package com.example.vibecap_back.domain.post;

import com.example.vibecap_back.domain.post.api.PostsApiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WebMvcTest(controllers = PostsApiController.class)
//@ActiveProfiles("test")
public class PostsApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void post() throws Exception {

    }

}
