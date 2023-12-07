package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
import et.digitalequb.frontend.dto.users.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class User {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/list-users"})
    public ModelAndView showHomeContents() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        et.digitalequb.frontend.entity.identity.User currentUser = getCurrentUser(token);
        System.out.println("User==============" + currentUser.getFullName());
        ModelAndView modelAndView = new ModelAndView("list-users");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("latestUsers", getUsers(token));
        return modelAndView;
    }
    @GetMapping({"/user"})
    public ModelAndView addEqub() {
        ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("user", new RegisterUserDto());
        return modelAndView;
    }

    @PostMapping({"/user-form"})
    public String addEqubForm(@ModelAttribute RegisterUserDto user) {
        System.out.println("Registering user obj");
        saveUser(user);
        return "redirect:/list-users";
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

    private et.digitalequb.frontend.entity.identity.User getCurrentUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<LoginUserDto> requestEntity =
                new HttpEntity<>(headers);
        ResponseEntity<et.digitalequb.frontend.entity.identity.User> loginResponse = restTemplate.exchange(
                environments.getBackendUrl() + "/users/user-details", HttpMethod.GET, requestEntity, et.digitalequb.frontend.entity.identity.User.class);
        return loginResponse.getBody();
    }

    private ResponseDto getUsers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewEqubList-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewEqubList");
        viewEqubtegnaDto.setRemark("Remark");
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ResponseDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/users/list", request, ResponseDto.class);
        return rsp.getBody();
    }
    private Equb saveUser( RegisterUserDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterUserDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/auth/signup", request, Equb.class);
        return rsp.getBody();
    }
}
