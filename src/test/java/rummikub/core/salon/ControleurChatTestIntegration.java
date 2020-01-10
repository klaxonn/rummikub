package rummikub.salon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ControleurChatTestIntegration {

  	@Autowired
  	private WebApplicationContext applicationContext;

  	private MockMvc mockMvc;

  	@BeforeEach
  	void setup() {
    this.mockMvc = MockMvcBuilders
            		.webAppContextSetup(applicationContext)
            		.build();
  }

}
