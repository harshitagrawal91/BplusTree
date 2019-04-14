/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author harsh
 */
public class ProcessInputFile {

    String fileName;
    PrintWriter writer = null;
    Tree tree;
    String outputFile = "output_file.txt";

    ProcessInputFile(String fileName, Tree tree) {
        this.fileName = fileName;
        this.tree = tree;
    }

    public void readFileAndHandleCommands() {
        FileOperation filehandler=new FileOperation(fileName);
        filehandler.loadFile();
         BufferedReader buffRead = filehandler.getBuffRead();
         writer=filehandler.getWriter();
          String commands = null;

        try {
            while ((commands = buffRead.readLine()) != null) {
                String str = commands.substring(0, commands.indexOf("("));
                //performing respective action based on the initial command string in file
                switch (str) {
                    case "Initialize":
                        int order = Integer.parseInt(commands.substring(commands.indexOf("(") + 1, commands.indexOf(")")));
                        tree.DEGREE = order;
                        break;
                    case "Insert":
                        Integer Insertkey = Integer.parseInt(commands.substring(commands.indexOf("(") + 1, commands.indexOf(",")));
                        Double value = Double.parseDouble(commands.substring(commands.indexOf(",") + 1, commands.indexOf(")")));
                        tree.insert(Insertkey, value);
                        break;
                    //handling both simple search and range of search   
                    case "Delete":
                        int key = Integer.parseInt(commands.substring(commands.indexOf("(") + 1, commands.indexOf(")")));
                        tree.delete(key);
                        break;
                    case "Search":
                        Integer searchType = commands.indexOf(",");
                        if (searchType >= 0) {
                            Integer keyLeft = Integer.parseInt(commands.substring(commands.indexOf("(") + 1, commands.indexOf(",")));
                            Integer keyRight = Integer.parseInt(commands.substring(commands.indexOf(",") + 1, commands.indexOf(")")));
                            StringBuilder outputStr = tree.rangeSearch(keyLeft, keyRight);
                            if (outputStr.toString().equals("")) {
                                writer.println("Null");
                            } else {

                                String range = outputStr.toString();
                                String trimmedRange = range.substring(0, (range.length() - 1));
                                writer.println(trimmedRange);
                            }
                        } else {
                            Integer keyToSearch = Integer.parseInt(commands.substring(commands.indexOf("(") + 1, commands.indexOf(")")));
                            Double output;
                            output = tree.search(keyToSearch);
                            if (output != null) {
                                String outputStr = output.toString().replace("[", "").replace("]", "");
                                writer.println(outputStr);
                            } else {
                                writer.println("Null");
                            }
                            break;
                        }
                }
            }
        } catch (UnsupportedEncodingException f) {
            System.out.println("Unsupported encoding at ");

        } catch (IOException exc) {
            System.out.println("IOException while reading the file or trying to close the file ");
        } finally {
            writer.close();
        }

    }
}
