package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.payments.ListPaymentsDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
import et.digitalequb.frontend.entity.identity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class Home {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/index", "/"})
    public ModelAndView showHomeContents() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        User currentUser = getCurrentUser(token);
        System.out.println("User==============" + currentUser.getFullName());
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("latestPayments", getPayments(token));
        return modelAndView;
    }

    private String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("kaleb.takele0810@gmail.com");
        loginUserDto.setPassword("kaleb");
        HttpEntity<LoginUserDto> request =
                new HttpEntity<>(loginUserDto, headers);
        ResponseEntity<LoginResponse> loginResponse = restTemplate.
                postForEntity(environments.getBackendUrl() + "/auth/login", request, LoginResponse.class);
        String token = "";
        if (Objects.nonNull(loginResponse.getBody()) && Objects.nonNull(loginResponse.getBody().getToken())) {
            token = loginResponse.getBody().getToken();
        }
        return token;
    }
    private User getCurrentUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<LoginUserDto> requestEntity =
                new HttpEntity<>(headers);
        ResponseEntity<User> loginResponse = restTemplate.exchange(
                environments.getBackendUrl() + "/users/user-details", HttpMethod.GET, requestEntity, User.class);
        return loginResponse.getBody();
    }

    private ListPaymentsDto getPayments(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewPayments-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewPayments");
        viewEqubtegnaDto.setRemark("Remark");
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ListPaymentsDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/payments/list", request, ListPaymentsDto.class);
        return rsp.getBody();
    }
}
