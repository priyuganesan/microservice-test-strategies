package com.cac.microservice.provider.pact;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.cac.microservice.provider.UserController;
import com.cac.microservice.provider.entity.User;
import com.cac.microservice.provider.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("provider")
@PactFolder("src/test/resources/pacts")
public class UserProviderPactTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void before(PactVerificationContext context) {
        MockitoAnnotations.openMocks(this);
        context.setTarget(new HttpTestTarget("localhost", 8124));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction(); // Verify the Pact interactions
    }

    @State("User with ID 124 exists")
    public void toCreateUserState() {
        // Simulate saving the user using the service
        User user = new User("124", "Jane Doe");
        when(userService.createUser(user)).thenReturn(user);
    }

    @PactVerifyProvider("A request to create user with ID 124")
    public String verifyPostUser() {
        // Create a new User object to simulate the response after POST
        User user = new User("124", "Jane Doe");
        // Return the expected response body as JSON
        return "{\"id\":\"" + user.getId() + "\", \"name\":\"" + user.getName() + "\"}";
    }

    @State("User with ID 123 exists")
    public void toGetState() {
        // Simulate retrieving the user using the service
        User user = new User("123", "John Doe");
        when(userService.getUserById("123")).thenReturn(user);
    }
}
