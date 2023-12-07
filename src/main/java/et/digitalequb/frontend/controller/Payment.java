package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equb.AddEqubtegna;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class Payment {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/list-payments"})
    public ModelAndView showHomeContents() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        User currentUser = getCurrentUser(token);
        System.out.println("User==============" + currentUser.getFullName());
        ModelAndView modelAndView = new ModelAndView("list-payments");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("latestPayments", getPayments(token));
        return modelAndView;
    }
    @GetMapping({"/payment"})
    public ModelAndView addPayment() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ModelAndView modelAndView = new ModelAndView("payment");
        modelAndView.addObject("payment", new AddEqubtegna());
        modelAndView.addObject("latestEqubs", getEqubs(token));
        return modelAndView;
    }

    @PostMapping({"/payment-form"})
    public String addPaymentForm(@ModelAttribute AddEqubtegna paymentObj) {
        System.out.println("Registering Payment obj");
        String token = getToken();
        System.out.println("Token==============" + token);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("AddPayment");
        registerDto.setRequestRefID("AddPayment-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(paymentObj);
        savePayment(token,registerDto);
        return "redirect:/list-payments";
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
    private ResponseDto getEqubs(String token) {
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
                postForEntity(environments.getBackendUrl() + "/equb/list", request, ResponseDto.class);
        return rsp.getBody();
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
    private Equb savePayment(String token, RegisterDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equb/add-payment", request, Equb.class);
        return rsp.getBody();
    }
}
