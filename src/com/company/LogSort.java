package com.company;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class LogSort
{

    public static void main(String[] args) throws FileNotFoundException
    {

        String currentline = null;
        StringBuilder sb = new StringBuilder();
        Boolean record = false;
        try {
            Workbook wb = new HSSFWorkbook();//Creating new spreadsheet page and all the headers.
                int rowcount = 0;
                Sheet logsheet = wb.createSheet();
                Row row = logsheet.createRow(rowcount++);
                row.createCell(0).setCellValue("Site ID");
                row.createCell(1).setCellValue("Error Code");
                row.createCell(2).setCellValue("Details");
            File folder = new File("C:\\Users\\adavoren\\Downloads\\mdaca_cache_seymourjohnson_test_agent_stats\\logs\\");//The folder I'm pulling all the files from.
            File[] listOfFiles = folder.listFiles();//Creating an array of files, storing to use
            Set<String> errorset = new HashSet<>();//Creating hashset for the errors, since each entry in a HashSet has to be unique, I am eliminating duplicate entries.
            for (File file : listOfFiles)
            {
                if (file.isFile())
                {
                    Scanner myReader =
                            new Scanner(file);
                    {
                        while (myReader.hasNextLine())
                        {
                            currentline = myReader.nextLine();

                            if (currentline.contains("[ERROR]")|| currentline.contains ("SQL"))//Narrowing down my search to lines of strings that contain both errors and sql.
                            {
                                String firstline = currentline;
                                currentline = myReader.nextLine();
                                if(currentline.contains("SQLCODE"))//Narrowing down my search even further, since the SQLCODE could be on the second or third line of a stacktrace, I moved the currentline variable to the next line.
                                {
                                    Row rowdata = logsheet.createRow(rowcount++);
                                    if(file.getName().contains("seymourjohnson"))
                                    {
                                        rowdata.createCell(0).setCellValue(("seymourjohnson"));
                                    }
                                    else
                                    {
                                        rowdata.createCell(0).setCellValue("Site Not Specified");

                                    }
                                    if(firstline.contains("[ERROR]"))
                                    {
                                    rowdata.createCell(1).setCellValue(StringUtils.substringAfter(firstline, "[ERROR]"));
                                    }
                                    else
                                        {
                                            rowdata.createCell(1).setCellValue(firstline);
                                    };
                                    rowdata.createCell(2).setCellValue(currentline + "\n" + myReader.nextLine() + "\n" + myReader.nextLine());
                                }

                            }
                        }

                    }
                }
            }

                    File file = new File("C:\\Users\\adavoren\\Downloads\\mdaca_cache_seymourjohnson_test_agent_stats\\errorsheetall.xls");//One worksheet where all the information goes.
                    FileOutputStream fo = new FileOutputStream(file);
                    wb.write(fo);
            }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
