package br.com.victoremerick.ilegra.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;

@Data
@Builder
public class SalesMan {

    public static final String REGEX = "001ç(\\d{11})ç(\\w+)ç([0-9\\.]+)";

    private String cpf;
    private String name;
    private double salary;
    private List<Sale> sales;

    public Integer getQuantitySales(){
        return Integer.valueOf(sales.size());
    }

    public static SalesMan from(MatchResult result){
        return SalesMan.builder()
                .cpf(result.group(1))
                .name(result.group(2))
                .salary(Double.parseDouble(result.group(3)))
                .sales(new ArrayList<>())
                .build();
    }
}
