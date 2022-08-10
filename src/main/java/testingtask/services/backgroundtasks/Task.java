package testingtask.services.backgroundtasks;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class Task<T> implements Callable<T> {
    final Class<T> type;
    private RestTemplate taskRestTemplate;
    private String url;

    public Task(Class<T> type, String url, RestTemplate restTemplate) {
        this.type = type;
        this.url = url;
        this.taskRestTemplate = restTemplate;
    }
    @Override
    public T call() throws Exception {
        HttpHeaders requestHeaders = new HttpHeaders();
        T response = taskRestTemplate.getForObject(url, type);
        return response;
    }
}
