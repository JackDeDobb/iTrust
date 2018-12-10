package edu.ncsu.csc.itrust.server;

import java.io.File;
import java.nio.file.Paths;

public class FilepathGen {
    public static String generateUniqueFileName(String filename) {
        long ts = System.nanoTime();
        return Paths.get(ts + "", filename).toString();
    }
}
