package com.example.Util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ExcelUtil {
    public static void setHeading(XSSFSheet xssfSheet, String heading []){
        Row headingRow = xssfSheet.createRow(0) ;
        Arrays.stream(heading).forEach(s -> headingRow.createCell(headingRow.getLastCellNum() == -1?0:headingRow.getLastCellNum()).setCellValue(s));

    }

    public static void writeExcel(XSSFWorkbook xssfWorkbook,String name){
        try {
            FileOutputStream fos = new FileOutputStream("./"+name+".xlsx");
            xssfWorkbook.write(fos);
            fos.close();
            xssfWorkbook.close();
            System.out.println("成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
