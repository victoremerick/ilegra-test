package br.com.victoremerick.ilegra.model;

import lombok.Builder;
import lombok.Data;

import java.util.regex.MatchResult;

@Data
@Builder
public class Client {

    public static final String REGEX = "002ç(\\d{14})ç([A-Za-z ]+)ç([A-Za-z]+)";

    private String cnpj;
    private String name;
    private String businessArea;

    public static Client from(MatchResult result){
        return Client.builder()
                .cnpj(result.group(1))
                .name(result.group(2))
                .businessArea(result.group(3))
                .build();
    }
}
