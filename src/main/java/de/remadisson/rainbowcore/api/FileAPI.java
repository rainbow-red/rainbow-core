package de.remadisson.rainbowcore.api;

import de.remadisson.rainbowcore.files;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class FileAPI {

    private final String filename;
    private final File file;

    /**
     * Initializes the API
     * @param filename
     * @param path
     */
    public FileAPI(String filename, String path) {
            this.filename = filename;
            File folder = new File(path);

            // Checking if Folder already exists
            if(!folder.exists()){
                folder.mkdir();
            }

            this.file = new File(folder, filename);

            //Check if File exists
            if(!file.exists()){
                try {
                    // Create new File
                    file.createNewFile();

                    // Debug message
                    System.out.println(files.console + "§a" + filename + " §ehas been created!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    /**
     * Returns the FileName
     * @return
     */
    public String getFilename(){
        return filename;
    }

    /**
     * Returns the Path of the File
     * @return
     */
    public String getPath(){
        return file.getPath();
    }

    /**
     * Returns the File for Raw adjustments
     * @return
     */
    public File getFile(){
        return file;
    }

    /**
     * Retruns true, if the File has been deleted!
     * @return
     */
    public boolean deleteFile(){
        return file.delete();
    }

    /**
     * Returns the Conent of the File
     * @return
     */
    public ArrayList<String> getContent() throws FileNotFoundException {
        ArrayList<String> content = new ArrayList<>();
        Scanner contentScanner = new Scanner(file);

        while(contentScanner.hasNextLine()){
            content.add(contentScanner.nextLine());
        }
        contentScanner.close();
        return content;
    }

    /**
     * Adds a Line to the given File.
     * @param line
     * @throws IOException
     */
    public void addContent(String line) throws IOException {
        FileWriter contentWriter = new FileWriter(file);
        contentWriter.write(line);
        contentWriter.close();
    }

}