package linker.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Symlinker{
    public static boolean createSymlink(String linkDir, String linkName, String original){
        return createSymlink(Paths.get(linkDir).resolve(linkName), Paths.get(original));
    }
    public static boolean createSymlink(Path link, File original){
        return createSymlink(link, original.toPath());
    }
    public static boolean createSymlink(Path link, Path original){
        try{
            Files.createSymbolicLink(link, original);
        }catch (IOException ioExc){
            ioExc.printStackTrace();
            return false;
        }
        return true;
    }
    public static String normalizeAndStringifyPath(String str){
        str = str.replace('\\', '/');
        StringBuilder builder = new StringBuilder(str);
        if(!str.startsWith("\"")){
            builder.insert(0, '\"');
        }
        if(!str.endsWith("\"")){
            builder.insert(builder.length(), '\"');
        }
        return builder.toString();
    }
}
