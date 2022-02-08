package br.com.victoremerick.ilegra.application;

import br.com.victoremerick.ilegra.filemanager.FileManager;
import br.com.victoremerick.ilegra.thread.ProcessorThread;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    public static int MAX_THREADS;

    private List<File> files;
    private int lastGetted;

    public static Application singleton;

    private Application(){}

    public static Application getInstance(){
        if(singleton == null) singleton = new Application();
        return singleton;
    }

    public void start(){
        files = FileManager.getAllFiles()
                .collect(Collectors.toList());
        lastGetted = 0;
        startThreads();
    }

    public synchronized File getFile(){
        if(lastGetted>=files.size()){
            return null;
        }
        return files.get(lastGetted++);
    }

    private void startThreads(){

        for(int i = 0; i < MAX_THREADS; i++){
            var thread = new ProcessorThread(this);
            thread.start();
        }
    }

}
