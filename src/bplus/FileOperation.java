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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harsh
 */
public class FileOperation {
    String outputFile="OutputFile.txt";
    String inputFile;
    PrintWriter writer = null;
    BufferedReader buffRead = null;

    public FileOperation(String inputFile) {
        this.inputFile=inputFile;
    }
    
    public void loadFile(){
    File file = new File(inputFile);
        FileReader fReader = null;      
        StringBuffer strBuff = new StringBuffer();
        try {
            fReader = new FileReader(file);
            writer = new PrintWriter(outputFile);
            buffRead = new BufferedReader(fReader);
        } catch (FileNotFoundException f) {
            System.out.println("Unable to find file " + f);
        }
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void setBuffRead(BufferedReader buffRead) {
        this.buffRead = buffRead;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getBuffRead() {
        return buffRead;
    }
    
}
