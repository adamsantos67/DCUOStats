package com.aksantos.dcuocensus;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.junit.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STGradientType;

import com.aksantos.dcuocensus.poi.GradientFill;

public class GradientFillTest {

    @Test
    public void testCreateGradientXSSFWorkbookEnumStringStringXSSFCellStyle() {
//        fail("Not yet implemented");
    }

    @Test
    public void testCreateGradientXSSFWorkbookEnumStringString() {
        /* Read Workbook and Access Worksheet */
        FileInputStream input_document = null;
        FileOutputStream out = null;
        try {
            input_document = new FileInputStream(new File("c2.xlsx"));
            XSSFWorkbook my_xlsx_workbook = new XSSFWorkbook(input_document);

            XSSFSheet sheet = my_xlsx_workbook.getSheetAt(0);

            Row row = sheet.createRow(0); // create Row
            XSSFCell cell1 = (XSSFCell) row.createCell(0);// Create Cell for
                                                          // Gradient Fill
            cell1.setCellValue("Linear Fill"); // Cell Data

            XSSFCell cell2 = (XSSFCell) row.createCell(1);// Create Cell for
            // Gradient Fill
            cell2.setCellValue("Path Fill"); // Cell Data

            int fillId1 = GradientFill.createGradient(my_xlsx_workbook, STGradientType.LINEAR, "FF0000", "CCECFF");
            System.out.println("fillId1 = " + fillId1);
            cell1.getCTCell().setS(fillId1);

            int fillId2 = GradientFill.createGradient(my_xlsx_workbook, STGradientType.PATH, "00FF00", "CCECFF");
            System.out.println("fillId2 = " + fillId2);

            cell2.getCTCell().setS(fillId2);

            List<XSSFCellFill> fills = my_xlsx_workbook.getStylesSource().getFills();
            for (XSSFCellFill fill : fills) {
                System.out.println("Fill: " + fill.getCTFill());
            }

            /* Write changes to the workbook */
            out = new FileOutputStream(new File("c22.xlsx"));
            my_xlsx_workbook.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Exception: " + e);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception: " + e);
        } finally {
            if (input_document != null) {
                try {
                    input_document.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
