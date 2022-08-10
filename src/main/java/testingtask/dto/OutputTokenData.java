package testingtask.dto;

import lombok.Data;

@Data
public class OutputTokenData implements OutputDatum {
    String value;
    int ttl;
}
