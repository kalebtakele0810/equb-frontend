package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
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
public class EqubType {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping({"/list-equb-type"})
    public ModelAndView ShowEqubTypeList() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ModelAndView modelAndView = new ModelAndView("list-equb-type");
        modelAndView.addObject("latestEqubTypes", getEqubsType(token));
        return modelAndView;
    }
    @GetMapping({"/equb-type"})
    public ModelAndView addEqubType() {
        ModelAndView modelAndView = new ModelAndView("equb-type");
        modelAndView.addObject("equbType", new et.digitalequb.frontend.entity.equb.EqubType());
        return modelAndView;
    }
    @PostMapping({"/equb-type-form"})
    public String addEqubTypeForm(@ModelAttribute et.digitalequb.frontend.entity.equb.EqubType equbType) {
        equbType.setStatus("1");
        String token=getToken();
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("AddEqubType");
        registerDto.setRequestRefID("AddEqubType-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(equbType);
        saveEqubType(token, registerDto);
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


    private Equb saveEqubType(String token, RegisterDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/config/add-equb-type", request, Equb.class);
        return rsp.getBody();
    }
    private ResponseDto getEqubsType(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        ViewEqubtegnaDto viewEqubtegnaDto = new ViewEqubtegnaDto();
        viewEqubtegnaDto.setRequestRefID("ViewEqubCategoryList-" + String.valueOf(UUID.randomUUID()));
        viewEqubtegnaDto.setCommandID("ViewEqubCategoryList");
        viewEqubtegnaDto.setRemark("Remark");
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(viewEqubtegnaDto, headers);
        ResponseEntity<ResponseDto> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/equb/list-equb-types", request, ResponseDto.class);
        return rsp.getBody();
    }
}
