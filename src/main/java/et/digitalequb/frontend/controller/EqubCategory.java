package et.digitalequb.frontend.controller;

import et.digitalequb.frontend.config.Environments;
import et.digitalequb.frontend.dto.RegisterDto;
import et.digitalequb.frontend.dto.ResponseDto;
import et.digitalequb.frontend.dto.equbtegna.ViewEqubtegnaDto;
import et.digitalequb.frontend.dto.users.LoginResponse;
import et.digitalequb.frontend.dto.users.LoginUserDto;
import et.digitalequb.frontend.entity.identity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class EqubCategory {

    @Autowired
    private Environments environments;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/list-equb-category"})
    public ModelAndView ShowEqubCategoryList() {
        String token = getToken();
        System.out.println("Token ==============" + token);
        ModelAndView modelAndView = new ModelAndView("list-equb-category");
        modelAndView.addObject("latestEqubCategories", getEqubsCategory(token));
        return modelAndView;
    }

    @GetMapping({"/equb-category"})
    public ModelAndView addEqubCategory() {
        ModelAndView modelAndView = new ModelAndView("equb-category");
        modelAndView.addObject("equbCategory", new et.digitalequb.frontend.entity.equb.EqubCategory());
        return modelAndView;
    }

    @GetMapping({"/view-equb-category"})
    public ModelAndView viewEqubCategory(@RequestParam String equbCategoryId) {
        ModelAndView modelAndView = new ModelAndView("view-equb-category");
        String token = getToken();
        System.out.println("Token ==============" + token);
        ViewEqubtegnaDto viewEqubCategory = new ViewEqubtegnaDto();
        viewEqubCategory.setRequestRefID("ViewEqubCategory-" + UUID.randomUUID());
        viewEqubCategory.setCommandID("ViewEqubCategory");
        viewEqubCategory.setRemark("Remark");
        viewEqubCategory.setIdentifier(equbCategoryId);
        et.digitalequb.frontend.entity.equb.EqubCategory equbCategory = viewEqubCategory(token, viewEqubCategory);
        modelAndView.addObject("equbCategory", equbCategory);
        return modelAndView;
    }

    @PostMapping({"/equb-category-form"})
    public String addEqubTypeForm(@ModelAttribute et.digitalequb.frontend.entity.equb.EqubCategory equbCateogry) {
        equbCateogry.setStatus("1");
        String token = getToken();
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("AddEqubCategory");
        registerDto.setRequestRefID("AddEqubCategory-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(equbCateogry);
        saveEqubCategory(token, registerDto, "/config/add-equb-category");
        return "redirect:/list-equb-category";
    }

    @PostMapping({"/edit-equb-category-form"})
    public String editEqubTypeForm(@ModelAttribute et.digitalequb.frontend.entity.equb.EqubCategory equbCateogry) {
        equbCateogry.setStatus("1");
        String token = getToken();
        RegisterDto registerDto = new RegisterDto();
        registerDto.setCommandID("EditEqubCategory");
        registerDto.setRequestRefID("EddEqubCategory-" + UUID.randomUUID());
        registerDto.setRemark("Org remark");
        registerDto.setTimestamp(new Date());
        registerDto.setSourceSystem("Web");
        registerDto.setVersion("V1.0");
        registerDto.setPayload(equbCateogry);
        saveEqubCategory(token, registerDto,"/config/edit-equb-category");
        return "redirect:/list-equb-type";
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


    private Equb saveEqubCategory(String token, RegisterDto registerdto, String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<RegisterDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<Equb> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + uri, request, Equb.class);
        return rsp.getBody();
    }

    private ResponseDto getEqubsCategory(String token) {
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
                postForEntity(environments.getBackendUrl() + "/equb/list-equb-category", request, ResponseDto.class);
        return rsp.getBody();
    }

    private et.digitalequb.frontend.entity.equb.EqubCategory viewEqubCategory(String token, ViewEqubtegnaDto registerdto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<ViewEqubtegnaDto> request =
                new HttpEntity<>(registerdto, headers);
        ResponseEntity<et.digitalequb.frontend.entity.equb.EqubCategory> rsp = restTemplate.
                postForEntity(environments.getBackendUrl() + "/config/view-equb-category",
                        request, et.digitalequb.frontend.entity.equb.EqubCategory.class);
        return rsp.getBody();
    }

}
