package br.com.victoremerick.ilegra.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
public class Sale {

    public static final String REGEX = "003ç(\\d+)ç\\[(.+)\\]ç([\\w]+)";
    private int id;
    private List<Product> products;
    private SalesMan salesMan;

    public Double getTotal(){
        return products.stream()
                .map(product -> product.getQuantity()*product.getPrice())
                .reduce(0.0, (product1, product2) -> product1+product2);
    }

    public static Sale from(MatchResult result, Stream<MatchResult> resultProducts, List<SalesMan> salesMan){
        var salesman = salesMan.stream()
                .filter(salesMan1 ->
                        salesMan1.getName().equals(result.group(3)))
                .findFirst()
                .get();

        var sale = Sale.builder()
                .id(Integer.parseInt(result.group(1)))
                .salesMan(salesman)
                .products(resultProducts.map(Product::from)
                        .collect(Collectors.toList()))
                .build();

        salesman.getSales().add(sale);
        return sale;
    }
}
