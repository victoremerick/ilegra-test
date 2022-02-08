package br.com.victoremerick.ilegra.thread;

import br.com.victoremerick.ilegra.application.Application;
import br.com.victoremerick.ilegra.filemanager.FileManager;
import br.com.victoremerick.ilegra.model.Client;
import br.com.victoremerick.ilegra.model.Product;
import br.com.victoremerick.ilegra.model.Sale;
import br.com.victoremerick.ilegra.model.SalesMan;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Data
public class ProcessorThread extends Thread{

    private static int THREAD_ID = 1;

    private int ID;

    private Application application;

    public File file;

    private Pattern pattern_client = Pattern.compile(Client.REGEX);
    private Pattern pattern_sale = Pattern.compile(Sale.REGEX);
    private Pattern pattern_salesman = Pattern.compile(SalesMan.REGEX);
    private Pattern pattern_product = Pattern.compile(Product.REGEX);

    List<Client> clients;
    List<Sale> sales;
    List<SalesMan> salesmans;

    public ProcessorThread(Application application){
        ID = THREAD_ID;
        THREAD_ID++;
        this.application = application;
    }

    public void reset(){
        salesmans = new ArrayList<>();
        sales = new ArrayList<>();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void run(){
        while((file = application.getFile())!=null) {
            System.out.println("Thread "+ID+" - Processing: "+file);
            reset();
            try {
                var linhas = FileManager.read(file);
                linhas.forEach(this::processLine);
                var content = prepareContentFile();
                String fileNameWithoutExt = FilenameUtils.getBaseName(file.getName());
                FileManager.write(fileNameWithoutExt + ".done.dat", content);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void processLine(String line){
        if(line.matches(Client.REGEX)){
            addClient(pattern_client.matcher(line).results().findAny().get());
        }else if(line.matches(SalesMan.REGEX)){
            addSalesMan(pattern_salesman.matcher(line).results().findAny().get());
        }else if(line.matches(Sale.REGEX)){
            addSale(pattern_sale.matcher(line).results().findAny().get());
        }
    }

    private void addSale(MatchResult result){
        var products_text = result.group(2);
        var resultsProducts = pattern_product.matcher(products_text)
                .results();
        var sale = Sale.from(result, resultsProducts, salesmans);
        sales.add(sale);
    }

    private void addSalesMan(MatchResult result){
        var salesMan = SalesMan.from(result);
        salesmans.add(salesMan);
    }

    private void addClient(MatchResult result){
        var client = Client.from(result);
        clients.add(client);
    }

    private Sale findMostExpensiveSale(){
        return sales.stream().max(Comparator.comparing(Sale::getTotal)).get();
    }

    private SalesMan findWorstSalesMan(){
        return salesmans.stream()
                .min(Comparator.comparing(SalesMan::getQuantitySales)).get();
    }

    public List<String> prepareContentFile(){
        List<String> lines = new ArrayList<>();
        lines.add(clients.size()+"");
        lines.add(salesmans.size()+"");
        lines.add(findMostExpensiveSale().getId()+"");
        lines.add(findWorstSalesMan().getName()+"");
        return lines;
    }

}
