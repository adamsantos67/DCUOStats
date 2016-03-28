package com.aksantos.dcuocensus.poi;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientStop;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STGradientType;

public class GradientFill {

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
