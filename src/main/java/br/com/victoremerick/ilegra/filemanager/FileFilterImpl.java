package br.com.victoremerick.ilegra.filemanager;

import java.io.File;
import java.io.FileFilter;

public class FileFilterImpl implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.getAbsolutePath().endsWith(".dat") && pathname.isFile();
    }
}
