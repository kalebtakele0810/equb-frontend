package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equb.AddEqubtegna;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.payments.ListPaymentsDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
import et.digitalequb.frontend.entity.equb.EqubCategory;
import et.digitalequb.frontend.entity.equb.EqubType;
import et.digitalequb.frontend.entity.identity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Controller
@Slf4j
@RequiredArgsConstructor
public class Equb {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/list-equb"})
    public ModelAndView showHomeContents() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        User currentUser = getCurrentUser(token);
        System.out.println("User==============" + currentUser.getFullName());
        ModelAndView modelAndView = new ModelAndView("list-equb");
        modelAndView.addObject("equb", currentUser);
        modelAndView.addObject("latestEqubs", getEqubs(token));
        return modelAndView;
    }

    @GetMapping({"/equb"})
    public ModelAndView addEqub() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ModelAndView modelAndView = new ModelAndView("equb");
        modelAndView.addObject("equb", new et.digitalequb.frontend.entity.equb.Equb());
        modelAndView.addObject("latestEqubTypes", getEqubTypes(token));
        modelAndView.addObject("latestEqubCategories", getEqubCategories(token));
        return modelAndView;
    }
    @GetMapping({"/view-equb"})
    public ModelAndView viewEqub(@RequestParam String equbId) {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewEqub-" + UUID.randomUUID());
        viewEqubtegnaDto.setCommandID("ViewEqub");
        viewEqubtegnaDto.setRemark("Remark");
        viewEqubtegnaDto.setIdentifier(equbId);
        ModelAndView modelAndView = new ModelAndView("view-equb");
        ResponseDto equb = viewEqub(token, viewEqubtegnaDto);
//        ResponseDto equbs = getEqubs(token);
        modelAndView.addObject("equb", equb);
        modelAndView.addObject("latestPayments", getPayments(token, equbId));
//        modelAndView.addObject("latestEqubs", equbs);
        AddEqubtegna addEqubtegna = new AddEqubtegna();
        addEqubtegna.setIdentifier(equbId);
        modelAndView.addObject("payment", addEqubtegna);
        return modelAndView;
    }
    @PostMapping({"/equb-form"})
    public String addEqubForm(@ModelAttribute et.digitalequb.frontend.entity.equb.Equb equb) {
        System.out.println("Registering equb obj");
        String token = getToken();
        System.out.println("Token ==============" + token);
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("AddEqub");
        registerDto.setRequestRefID("AddEqub-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(equb);
        saveEqubs(token, registerDto);
        return "redirect:/list-equb";
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

    private ResponseDto getEqubTypes(String token) {
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
                postForEntity(environments.getBackendUrl() + "/equb/list-equb-types", request, ResponseDto.class);
        return rsp.getBody();
    }

    private ResponseDto getEqubCategories(String token) {
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
                postForEntity(environments.getBackendUrl() + "/equb/list-equb-category", request, ResponseDto.class);
        return rsp.getBody();
    }
    private ResponseDto viewEqub(String token, ViewEqubtegnaDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<ResponseDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equb/view-equb", request, ResponseDto.class);
        return rsp.getBody();
    }
    private Equb saveEqubs(String token, RegisterDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equb/add", request, Equb.class);
        return rsp.getBody();
    }

    private ListPaymentsDto getPayments(String token, String equbId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewPayments-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewPayments");
        viewEqubtegnaDto.setRemark("Remark");
        viewEqubtegnaDto.setIdentifier(equbId);
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ListPaymentsDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/payments/list-by-equb", request, ListPaymentsDto.class);
        ListPaymentsDto listPaymentsDto = rsp.getBody();
        if (Objects.isNull(listPaymentsDto)) {
            listPaymentsDto = new ListPaymentsDto();
        }
        return listPaymentsDto;
    }
}
