package dev.levelupschool.backend;

import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.SubscriptionRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.subscription.SubscriptionService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubscriptionServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;


    @Test
    public void givenUser_whenSubscribe_thenMakePremium() throws Exception {
        var authenticatedUser = userRepository.save(new User("johndoe@gmail.com", "John Uzodinma",  "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);

        JSONObject jsonObject = new JSONObject("{'error': False, 'status': 'success', 'validationRequired': False, 'txRef': 'MC-1703038981835', 'flwRef': 'FLW-MOCK-fe652982c08e124528277a3a2b9d1037', 'suggestedAuth': None, 'authUrl': 'https://ravesandboxapi.flutterwave.com/mockvbvpage?ref=FLW-MOCK-fe652982c08e124528277a3a2b9d1037&code=00&message=Approved. Successful&receiptno=RN1703038983175'}");
        subscriptionService.subscribe(authenticatedUser, jsonObject, "monthly");

        Assertions.assertTrue(authenticatedUser.isPremium());

    }
}
