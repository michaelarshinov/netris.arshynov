package testingtask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import testingtask.dto.OutputDto;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringAggregationRestControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;


    private List<OutputDto> getItems(int timeout) {
        HttpHeaders requestHeaders = new HttpHeaders();
        ResponseEntity<List<OutputDto>> entityTimeout1 =
                this.restTemplate.exchange("http://localhost:" + port+"/list?timeout="+timeout, HttpMethod.GET,
                        new HttpEntity<Object>(requestHeaders), new ParameterizedTypeReference<List<OutputDto>>() { });
        return entityTimeout1.getBody();
    }

    @Test
    public void shouldPass() {
        assertThat(getItems(1).size() < getItems(1000).size());
    }
}
