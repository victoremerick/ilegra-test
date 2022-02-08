package br.com.victoremerick.ilegra;

import br.com.victoremerick.ilegra.application.Application;
import br.com.victoremerick.ilegra.filemanager.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String args[]){

        Application.MAX_THREADS = Runtime.getRuntime().availableProcessors();

        if(args.length > 0){
            try{
                Application.MAX_THREADS = 2; Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.err.println("Parameter must be a integer number.");
                System.exit(0);
            }
        }

        Application.getInstance().start();
    }
}
