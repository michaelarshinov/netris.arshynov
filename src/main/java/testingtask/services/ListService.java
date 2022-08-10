package testingtask.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testingtask.dto.*;
import testingtask.services.backgroundtasks.Task;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ListService {

    @Value("${task.max.ttl.ms}")
    private int taskTimeout;
    private static final Logger log = LoggerFactory.getLogger(ListService.class);

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    public List<OutputDto> read(String camerasServiceURL, int timeout) {
        HttpHeaders requestHeaders = new HttpHeaders();
        ResponseEntity<List<InputDto>> entity =
                restTemplate.exchange(camerasServiceURL, HttpMethod.GET,
                        new HttpEntity<Object>(requestHeaders), new ParameterizedTypeReference<List<InputDto>>() { });
        List<InputDto> items = entity.getBody();
        List<OutputDto> print = new LinkedList<>();
        if (!items.isEmpty()) {
            Map<Long, Future<OutputDatum>> futureSources = new Hashtable<>();
            Map<Long, Future<OutputDatum>> futureTokens = new Hashtable<>();
            ExecutorService executor = Executors.newFixedThreadPool(items.size());
            //items.parallelStream().forEach(x-> )

            for (InputDto item: items) {
                futureSources.put(item.getId(), executor.submit(new Task(OutputSourceData.class, item.getSourceDataUrl(), restTemplate)));
                futureTokens.put(item.getId(), executor.submit(new Task(OutputTokenData.class, item.getTokenDataUrl(), restTemplate)));
            }
            if ( timeout == 0 ) {
                timeout = taskTimeout;
            }
            try {
                Thread.sleep(timeout*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (InputDto item: items) {
                try {
                    OutputSourceData source = (OutputSourceData)
                            futureSources.get(item.getId()).get(taskTimeout, TimeUnit.MILLISECONDS);
                    OutputTokenData token = (OutputTokenData)
                            futureTokens.get(item.getId()).get(taskTimeout, TimeUnit.MILLISECONDS);
                    print.add(OutputDto.builder()
                            .id(item.getId())
                            .urlType(source.getUrlType())
                            .videoUrl(source.getVideoUrl())
                            .value(token.getValue())
                            .ttl(token.getTtl())
                            .build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    //e.printStackTrace();
                    log.error("timeout error for camera with id"+item.getId());
                }
            }
        }
        return print;
    }
}
