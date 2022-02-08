package br.com.victoremerick.ilegra.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.MatchResult;

@Data
@Builder
public class Product {

    public static final String REGEX = "(\\d+)-(\\d+)-([0-9\\.]+)";

    private int id;
    private int quantity;
    private double price;

    public static Product from(MatchResult matchResult){
        return Product.builder()
                .id(Integer.parseInt(matchResult.group(1)))
                .quantity(Integer.parseInt(matchResult.group(2)))
                .price(Double.parseDouble(matchResult.group(3)))
                .build();
    }
}
