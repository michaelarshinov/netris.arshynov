package testingtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import testingtask.dto.OutputDto;
import testingtask.services.ListService;
import testingtask.dto.InputDto;

import java.util.List;

@RestController
public class AggregateRestController {
    @Value("${cameras.service.url}")
    private String camerasServiceURL;

    @Autowired
    private ListService listService;

/*    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }*/
    @Bean(name="restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/list")
    public List<OutputDto> list(@RequestParam(value = "timeout") int timeout) {
        List<OutputDto> list = listService.read(camerasServiceURL, timeout);
        return list;
    }
}
