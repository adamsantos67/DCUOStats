package com.aksantos.dcuocensus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientStop;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STGradientType;

public class GradientFill {

    public static void main(String[] args) throws Exception {
        GradientFill gf = new GradientFill();

        gf.execute();
    }

    private void execute() throws FileNotFoundException, IOException {
        /* Read Workbook and Access Worksheet */
        FileInputStream input_document = new FileInputStream(new File("c2.xlsx"));
        XSSFWorkbook my_xlsx_workbook = new XSSFWorkbook(input_document);

        XSSFSheet sheet = my_xlsx_workbook.getSheetAt(0);

        Row row = sheet.createRow(0); // create Row
        XSSFCell cell1 = (XSSFCell) row.createCell(0);// Create Cell for
                                                      // Gradient Fill
        cell1.setCellValue("Linear Fill"); // Cell Data

        XSSFCell cell2 = (XSSFCell) row.createCell(1);// Create Cell for
        // Gradient Fill
        cell2.setCellValue("Path Fill"); // Cell Data

        int fillId1 = createGradient(my_xlsx_workbook, STGradientType.LINEAR, "FF0000", "CCECFF");
        System.out.println("fillId1 = " + fillId1);
        cell1.getCTCell().setS(fillId1);

        int fillId2 = createGradient(my_xlsx_workbook, STGradientType.PATH, "00FF00", "CCECFF");
        System.out.println("fillId2 = " + fillId2);

        cell2.getCTCell().setS(fillId2);

        List<XSSFCellFill> fills = my_xlsx_workbook.getStylesSource().getFills();
        for (XSSFCellFill fill : fills) {
            System.out.println("Fill: " + fill.getCTFill());
        }

        /* Write changes to the workbook */
        FileOutputStream out = new FileOutputStream(new File("c22.xlsx"));
        my_xlsx_workbook.write(out);
        out.close();
    }

    public static int createGradient(XSSFWorkbook my_xlsx_workbook, STGradientType.Enum type, String color1,
            String color2) {
        return createGradient(my_xlsx_workbook, type, color1, color2, null);
    }

    public static int createGradient(XSSFWorkbook my_xlsx_workbook, STGradientType.Enum type, String color1,
            String color2, XSSFCellStyle style) {
        /* Add Fills with Gradient Information */
        CTFill myGradientFill = CTFill.Factory.newInstance(); /// * Create a
                                                              /// Gradient fill
        CTGradientFill gFill = myGradientFill.addNewGradientFill(); /// * Set
                                                                    /// Type,
                                                                    /// left,
                                                                    /// right,
                                                                    /// top and
                                                                    /// bottom
                                                                    /// values
        gFill.setType(type);
        if (type == STGradientType.PATH) {
            double d = 0.5;
            gFill.setLeft(d);
            gFill.setRight(d);
            gFill.setTop(d);
            gFill.setBottom(d);
        } else {
            gFill.setDegree(270);
        }

        /* Add Stop Position */
        CTGradientStop pos0 = gFill.addNewStop();
        pos0.setPosition(0.0);

        /* Set Color 1 */
        CTColor stopPositionColor = pos0.addNewColor();
        stopPositionColor.setRgb(javax.xml.bind.DatatypeConverter.parseHexBinary(color1));

        /* Add Stop Position */
        CTGradientStop pos1 = gFill.addNewStop();
        pos1.setPosition(1.0);

        /* Set Color 2 */
        CTColor stopPositionColor1 = pos1.addNewColor();
        stopPositionColor1.setRgb(javax.xml.bind.DatatypeConverter.parseHexBinary(color2));

        XSSFCellFill myCellFill = new XSSFCellFill(myGradientFill);
        int index = my_xlsx_workbook.getStylesSource().putFill(myCellFill);

        CTXf addCTX = null;
        if (style == null) {
            addCTX = CTXf.Factory.newInstance();
            addCTX.setNumFmtId(0L);
            addCTX.setFontId(0L);
            addCTX.setBorderId(0L);
            addCTX.setXfId(0L);
        } else {
            CTXf curCTX = style.getCoreXf();
            addCTX = (CTXf) curCTX.copy();
        }
        addCTX.setFillId(index);
        addCTX.setApplyFill(true);
        
        int styleId = my_xlsx_workbook.getStylesSource().putCellXf(addCTX);

        return styleId - 1;
    }
}
