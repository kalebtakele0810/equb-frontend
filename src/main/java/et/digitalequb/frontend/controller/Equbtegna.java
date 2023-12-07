package et.digitalequb.frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equb.AddEqubtegna;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.payments.ListPaymentsDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
import et.digitalequb.frontend.entity.equb.StartEqub;
import et.digitalequb.frontend.entity.identity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
@RequiredArgsConstructor
public class Equbtegna {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/list-equbtegna"})
    public ModelAndView showHomeContents() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        User currentUser = getCurrentUser(token);
        System.out.println("User==============" + currentUser.getFullName());
        ModelAndView modelAndView = new ModelAndView("list-equbtegna");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("latestEqubtegnas", getEqubtegnas(token));
        return modelAndView;
    }

    @GetMapping({"/equbtegna"})
    public ModelAndView addEqubtegna() {
        ModelAndView modelAndView = new ModelAndView("equbtegna");
        modelAndView.addObject("equbtegna", new et.digitalequb.frontend.entity.equbtegna.Equbtegna());
        return modelAndView;
    }

    @GetMapping({"/view-equbtegna"})
    public ModelAndView viewEqubtegna(@RequestParam String equbtegnaNumber) {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewEqubtegna-" + UUID.randomUUID());
        viewEqubtegnaDto.setCommandID("ViewEqubtegna");
        viewEqubtegnaDto.setRemark("Remark");
        viewEqubtegnaDto.setIdentifier(equbtegnaNumber);
        ModelAndView modelAndView = new ModelAndView("view-equbtegna");
        ResponseDto equbtegna = viewEqubtegna(token, viewEqubtegnaDto);
        ResponseDto equbs = getEqubs(token);
        modelAndView.addObject("equbtegna", equbtegna);
        modelAndView.addObject("latestPayments", getPayments(token, equbtegnaNumber));
        modelAndView.addObject("latestEqubs", equbs);
        AddEqubtegna addEqubtegna = new AddEqubtegna();
        addEqubtegna.setIdentifier(equbtegnaNumber);
        modelAndView.addObject("payment", addEqubtegna);
        return modelAndView;
    }

    @PostMapping({"/equbtegna-form"})
    public String addEqubForm(@ModelAttribute et.digitalequb.frontend.entity.equbtegna.Equbtegna equbtegna) {
        System.out.println("Registering Equbtegna obj");
        String token = getToken();
        System.out.println("Token ==============" + token);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("AddEqubtegna");
        registerDto.setRequestRefID("AddEqubtegna-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(equbtegna);
        saveEqubtegna(token, registerDto);
        return "redirect:/list-equbtegna";
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

    private ResponseDto getEqubtegnas(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewEqubtegnaList-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewEqubtegnaList");
        viewEqubtegnaDto.setRemark("Remark");
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ResponseDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equbtegnas/list", request, ResponseDto.class);
        return rsp.getBody();
    }

    private Equb saveEqubtegna(String token, RegisterDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equbtegnas/add", request, Equb.class);
        return rsp.getBody();
    }

    private ResponseDto viewEqubtegna(String token, ViewEqubtegnaDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<ResponseDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equbtegnas/view", request, ResponseDto.class);
        return rsp.getBody();
    }

    private ListPaymentsDto getPayments(String token, String msisdn) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewPayments-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewPayments");
        viewEqubtegnaDto.setRemark("Remark");
        viewEqubtegnaDto.setIdentifier(msisdn);
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ListPaymentsDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/payments/list-by-customer", request, ListPaymentsDto.class);
        ListPaymentsDto listPaymentsDto = rsp.getBody();
        if (Objects.isNull(listPaymentsDto)) {
            listPaymentsDto = new ListPaymentsDto();
        }
        return listPaymentsDto;
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
}
