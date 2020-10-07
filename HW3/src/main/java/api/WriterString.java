package api;

import java.io.*;
import java.net.URISyntaxException;

public class WriterString{

    private final String path;
    private String stringData;

    public WriterString(String stringName, String stringData) throws URISyntaxException, IOException {
        this.path=createDirectory();
        this.stringData=stringData;
        this.writeString(stringName, stringData);
    }

    private String createDirectory() throws URISyntaxException {
        File directory = new File(getDirectory());
        if(!directory.exists()){
            directory.mkdirs();
        }
        return getDirectory();
    }
    private static String getDirectory() throws URISyntaxException{
        String path = ClassLoader.getSystemClassLoader().getResource("").toURI().getPath();
        path=path.substring(0,path.indexOf("classes"));
        path+="resources" + File.separator + "api" +File.separator;
        return path;
    }

    public String printPath(){
        return this.path;
    }

    private void writeString(String fileName, String fileData) throws URISyntaxException, IOException {
        FileWriter out = new FileWriter(printPath()+fileName);
        out.write(fileData);
        out.close();
        System.out.println("file created:");
        System.out.println(fileData);
        }

}