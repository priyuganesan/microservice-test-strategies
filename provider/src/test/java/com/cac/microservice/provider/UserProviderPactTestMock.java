//package com.cac.microservice.provider;
//
//import au.com.dius.pact.provider.PactVerifyProvider;
//import au.com.dius.pact.provider.junit5.HttpTestTarget;
//import au.com.dius.pact.provider.junit5.PactVerificationContext;
//import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
//import au.com.dius.pact.provider.junitsupport.Provider;
//import au.com.dius.pact.provider.junitsupport.State;
//import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
//import com.cac.microservice.provider.entity.User;
//import com.cac.microservice.provider.repository.UserRepository;
//import jakarta.persistence.EntityManagerFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.TestTemplate;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.sql.DataSource;
//
//import static org.mockito.BDDMockito.given;
//
//@WebMvcTest
//@Provider("provider")
//@PactFolder("src/test/resources/pacts")
//public class UserProviderPactTestMock {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void before(PactVerificationContext context) {
//        context.setTarget(new HttpTestTarget("localhost", 8124));
//    }
//
//    @TestTemplate
//    @ExtendWith(PactVerificationInvocationContextProvider.class)
//    void pactVerificationTestTemplate(PactVerificationContext context) {
//        context.verifyInteraction(); // Verify the Pact interactions
//    }
//
//    // Simulating a state where a user with a specific ID exists
//    @State("User with ID 124 exists")
//    public void toCreateUserState() {
//        // Simulate saving the user to the database (through the mock repository)
//
//        User user = new User("124", "Jane Doe");
//        userRepository = Mockito.mock(UserRepository.class); // Mock the repository
//        given(userRepository.save(user)).willReturn(user); // Mock save method behavior
//    }
//
//    // Simulating a POST request to create a user
//    @PactVerifyProvider("A request to create user with ID 124")
//    public String verifyPostUser() throws Exception {
//        User user = new User("124", "Jane Doe");
//        // The controller should handle this request and return the expected result
//        return "{\"id\":\"" + user.getId() + "\", \"name\":\"" + user.getName() + "\"}";
//    }
//
//    // Simulating a state where a user with ID 123 exists
//    @State("User with ID 123 exists")
//    public void toGetState() {
//        // Simulate saving a user with ID 123 for the GET request verification
//        User user = new User("123", "John Doe");
//        userRepository = Mockito.mock(UserRepository.class); // Mock the repository
//        given(userRepository.save(user)).willReturn(user); // Mock save method behavior
//    }
//}
//
//
//
