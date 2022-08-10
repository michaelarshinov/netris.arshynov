package testingtask.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OutputDto {
    long id;
    String urlType;
    String videoUrl;
    String value;
    int ttl;
}
