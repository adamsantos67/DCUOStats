package com.aksantos.dcuocensus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.examples.AddDimensionedImage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STGradientType;

import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.Item;
import com.aksantos.dcuocensus.models.Personality;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.EquipmentSlot;
import com.aksantos.dcuocensus.models.enums.Gender;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Power;
import com.aksantos.dcuocensus.models.enums.Role;

public class XLSXWriter {
    private static final String STYLE_STARTS[] = { "Collect all styles in the ", "Collect all styles of the ",
            "Collect all items in the ", " style in the ", " styles in the ", "Collect a complete ",
            "Collect all styles for the " };
    private static final String STYLE_END = " set";
    private static final String STYLE_END2 = " in the classic game";

    private static final String LEGENDS_STARTS[] = { "Events as a ", "Events as any " };
    private static final String LEGENDS_ENDS[] = { "-related", " character" };

    private static final String EPISODE_START = "omplete the ";
    private static final String EPISODE_START2 = "uring the ";
    private static final String EPISODE_START3 = "hrough the ";
    private static final String EPISODE_START4 = "in the ";

    private static final String EPISODE_OP = " operation";
    private static final String EPISODE_OP2 = " Operation";
    private static final String EPISODE_ALERT = " alert";
    private static final String EPISODE_ALERT2 = " Alert";
    private static final String EPISODE_RAID = " raid";
    private static final String EPISODE_DUO = " duo";
    private static final String EPISODE_DUO2 = " Duo";
    private static final String EPISODE_SOLO = " solo";
    private static final String EPISODE_MISSION = " mission";

    private XSSFWorkbook wb;

    private XSSFCellStyle cs = null;
    private XSSFCellStyle cs1 = null;
    private XSSFCellStyle cs2 = null;
    private XSSFCellStyle csCenter = null;
    private XSSFCellStyle csVertCenter = null;
    private XSSFCellStyle csBold = null;
    private XSSFCellStyle csHide = null;
    private XSSFCellStyle csNumeric = null;
    private XSSFCellStyle csRed = null;
    private XSSFCellStyle csRedCenter = null;
    private XSSFCellStyle csRose = null;
    private XSSFCellStyle csRoseCenter = null;
    private XSSFCellStyle csGreen = null;
    private XSSFCellStyle csGreenCenter = null;
    private XSSFCellStyle csPaleBlue = null;
    private XSSFCellStyle csPaleBlueCenter = null;
    private XSSFCellStyle csLightBlue = null;
    private XSSFCellStyle csLavender = null;
    private XSSFCellStyle csYellow = null;
    private XSSFCellStyle csYellowCenter = null;
    private XSSFCellStyle csOrange = null;
    private XSSFCellStyle csOrangeCenter = null;
    private XSSFCellStyle csGrey = null;
    private XSSFCellStyle csGold = null;

    private int fillLtBlue = 0;
    private int fillGreen = 0;
    private int fillDkBlue = 0;
    private int fillPurple = 0;
    private int fillGold = 0;

    private int fillAtomic = 0;
    private int fillCelestial = 0;
    private int fillEarth = 0;
    private int fillElectricity = 0;
    private int fillFire = 0;
    private int fillGadgets = 0;
    private int fillGreenLight = 0;
    private int fillIce = 0;
    private int fillMental = 0;
    private int fillMunitions = 0;
    private int fillNature = 0;
    private int fillQuantum = 0;
    private int fillRage = 0;
    private int fillSorcery = 0;
    private int fillYellowLight = 0;

    private Font f = null;
    private Font fBold = null;

    public XLSXWriter() {
        wb = new XSSFWorkbook();
        createStyles();
    }

    public void writeFile() {
        writeFile("DCUOStats.xlsx");
    }

    public void writeFile(String filename) {
        FileOutputStream neededFile = null;
        try {
            neededFile = new FileOutputStream(filename);

            wb.write(neededFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wb = null;
            }

            if (neededFile != null) {
                try {
                    neededFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                neededFile = null;
            }
        }
    }

    private void createStyles() {
        cs = wb.createCellStyle();
        f = wb.createFont();
        f.setFontHeightInPoints((short) 11);
        cs.setFont(f);

        csHide = wb.createCellStyle();
        csHide.setFont(f);
        csHide.setHidden(true);

        csCenter = wb.createCellStyle();
        csCenter.setFont(f);
        csCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csVertCenter = wb.createCellStyle();
        csVertCenter.setFont(f);
        csVertCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        cs1 = wb.createCellStyle();
        fBold = wb.createFont();
        fBold.setFontHeightInPoints((short) 11);
        fBold.setBold(true);
        cs1.setAlignment(CellStyle.ALIGN_CENTER);
        cs1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs1.setFont(fBold);

        cs2 = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        cs2.setDataFormat(df.getFormat("#,##0"));
        cs2.setFont(f);

        csNumeric = wb.createCellStyle();
        DataFormat df2 = wb.createDataFormat();
        csNumeric.setDataFormat(df2.getFormat("#"));
        csNumeric.setFont(f);

        csBold = wb.createCellStyle();
        csBold.setFont(fBold);

        csRed = wb.createCellStyle();
        csRed.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        csRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csRed.setFont(f);

        csRedCenter = (XSSFCellStyle) csRed.clone();
        csRedCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csRedCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csRose = wb.createCellStyle();
        csRose.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        csRose.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csRose.setFont(f);

        csRoseCenter = (XSSFCellStyle) csRose.clone();
        csRoseCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csRoseCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csGreen = wb.createCellStyle();
        csGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        csGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csGreen.setFont(f);

        csGreenCenter = (XSSFCellStyle) csGreen.clone();
        csGreenCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csGreenCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csPaleBlue = wb.createCellStyle();
        csPaleBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        csPaleBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csPaleBlue.setFont(f);

        csPaleBlueCenter = (XSSFCellStyle) csPaleBlue.clone();
        csPaleBlueCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csPaleBlueCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csLightBlue = wb.createCellStyle();
        csLightBlue.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        csLightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csLightBlue.setFont(f);

        csLavender = wb.createCellStyle();
        csLavender.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        csLavender.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csLavender.setFont(f);

        csYellow = wb.createCellStyle();
        csYellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        csYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csYellow.setFont(f);

        csYellowCenter = (XSSFCellStyle) csYellow.clone();
        csYellowCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csYellowCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csOrange = wb.createCellStyle();
        csOrange.setFillForegroundColor(IndexedColors.TAN.getIndex());
        csOrange.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csOrange.setFont(f);

        csOrangeCenter = (XSSFCellStyle) csOrange.clone();
        csOrangeCenter.setAlignment(CellStyle.ALIGN_CENTER);
        csOrangeCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        csGrey = wb.createCellStyle();
        csGrey.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        csGrey.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csGrey.setFont(f);

        csGold = wb.createCellStyle();
        csGold.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        csGold.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csGold.setFont(f);

        // 168, 232, 255 - 30, 55, 77
        // A8E8FF 1E374D
        fillLtBlue = GradientFill.createGradient(wb, STGradientType.LINEAR, "A8E8FF", "1E374D");
        System.out.println("fillLtBlue: " + fillLtBlue);

        // 202, 243, 111 - 17, 40, 46
        // CAF36F 11282E
        fillGreen = GradientFill.createGradient(wb, STGradientType.LINEAR, "CAF36F", "11282E");
        System.out.println("fillGreen: " + fillGreen);

        // 17, 76, 230 - 0, 6, 80
        // 114CE6 000650
        fillDkBlue = GradientFill.createGradient(wb, STGradientType.LINEAR, "114CE6", "000650");
        System.out.println("fillDkBlue: " + fillDkBlue);

        // 130, 20, 215 - 45, 1, 112
        // 8214D7 2D0170
        fillPurple = GradientFill.createGradient(wb, STGradientType.LINEAR, "8214D7", "2D0170");
        System.out.println("fillPurple: " + fillPurple);

        // 251, 235, 87 - 241, 206, 80 - 53, 42, 0
        // FBEB57 F1CE50 352A00
        fillGold = GradientFill.createGradient(wb, STGradientType.LINEAR, "FCEC63", "5A440B");
        System.out.println("fillGold: " + fillGold);

        STGradientType.Enum type = STGradientType.LINEAR;
        fillAtomic = GradientFill.createGradient(wb, type, "ffffff"/* "dcc554" */, "c9740c", cs1);
        fillCelestial = GradientFill.createGradient(wb, type, "ffffff", "806000", cs1);
        fillGadgets = GradientFill.createGradient(wb, type, "ffffff", "6080ff", cs1);
        fillEarth = GradientFill.createGradient(wb, type, "ffffff", "404040", cs1);
        fillElectricity = GradientFill.createGradient(wb, type, "ffffff", "c0a040", cs1);
        fillFire = GradientFill.createGradient(wb, type, "ffffff", "c9740c", cs1);
        fillIce = GradientFill.createGradient(wb, type, "ffffff", "c0c0ff", cs1);
        fillGreenLight = GradientFill.createGradient(wb, type, "ffffff" /* "c1d895" */, "72a625", cs1);
        fillMental = GradientFill.createGradient(wb, type, "ffffff", "800080", cs1);
        fillMunitions = GradientFill.createGradient(wb, type, "ffffff", "206000", cs1);
        fillNature = GradientFill.createGradient(wb, type, "5b7339", "7ca648", cs1);
        fillQuantum = GradientFill.createGradient(wb, type, "d7eff5"/* "80cbe0" */, "949fe1", cs1);
        fillRage = GradientFill.createGradient(wb, type, "ffffff"/* "da6f77" */, "932322", cs1);
        fillSorcery = GradientFill.createGradient(wb, type, "ffffff", "c00080", cs1);
        fillYellowLight = GradientFill.createGradient(wb, type, "ffffff", "ded57b", cs1);

        StylesTable stylesTable = wb.getStylesSource();
        List<XSSFCellFill> fills = stylesTable.getFills();
        for (int i = 0; i < fills.size(); i++) {
            XSSFCellFill fill = fills.get(i);
            System.out.println("Fill[" + i + "]: " + fill.getCTFill());
        }

        for (int idx = 0; idx < stylesTable.getNumCellStyles(); idx++) {
            CTXf xf = stylesTable.getCellXfAt(idx);
            System.out.println("xf[" + idx + "]: " + xf);
        }
    }

    public void writeCharacterItems(Collection<Character> characters, Map<Long, Item> items) {
        XSSFSheet sheet = wb.createSheet("CharacterItems");

        Row r = null;
        XSSFCell c = null;

        int rownum = 0;
        r = sheet.createRow(rownum++);

        int cellnum = 0;
        String headers[] = { "Slot", "Im", "Name", "Level", "Category", "Sub-Category", "Quality", "Role", "Alignment",
                "Id" };

        for (String header : headers) {
            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs1);
            c.setCellValue(header);
        }

        for (Character character : characters) {
            r = sheet.createRow(rownum++);

            cellnum = 0;

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(csBold);
            c.setCellValue(character.getName());

            for (CharactersItem charItem : character.getCharacterItems()) {
                r = sheet.createRow(rownum++);

                cellnum = 0;

                c = (XSSFCell) r.createCell(cellnum++);
                c.setCellStyle(cs);
                c.setCellValue(EquipmentSlot.getById(charItem.getEquipmentSlotId()).toString());

                long itemId = charItem.getItemId();
                Item item = items.get(itemId);

                if (item == null) {
                    c = (XSSFCell) r.createCell(headers.length - 1);
                    c.setCellStyle(csNumeric);
                    c.setCellValue(itemId);
                } else {
                    writeItemRow(sheet, r, rownum, cellnum, item);
                }
            }
            r = sheet.createRow(rownum++);
        }
        /*
         * for (IndexedColors color : IndexedColors.values()) { r =
         * sheet.createRow(rownum++); c = (XSSFCell) r.createCell(2);
         * XSSFCellStyle csColor = wb.createCellStyle();
         * csColor.setFillForegroundColor(color.getIndex());
         * csColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         * csColor.setFont(f); c.setCellValue(color.toString());
         * c.setCellStyle(csColor); }
         */
    }

    private void writeItemRow(XSSFSheet sheet, Row r, int rownum, int cellnum, Item item) {
        XSSFCell c;
        if (item.getIconId() > 0) {
            try {
                new AddDimensionedImage().addImageToSheet(cellnum++, rownum - 1, sheet, sheet.createDrawingPatriarch(),
                        new File("Icon" + item.getIconId() + ".png").toURI().toURL(), 24, 24,
                        AddDimensionedImage.EXPAND_ROW_AND_COLUMN);
                c = (XSSFCell) r.createCell(cellnum - 1);
                if (c != null) {
                    switch (item.getQuality()) {
                    case 0:
                        c.setCellStyle(csGrey);
                        break;
                    case 1:
                        c.getCTCell().setS(fillLtBlue);
                        break;
                    case 2:
                        c.getCTCell().setS(fillGreen);
                        break;
                    case 4:
                        c.getCTCell().setS(fillDkBlue);
                        break;
                    case 5:
                        c.getCTCell().setS(fillPurple);
                        break;
                    case 6:
                        c.getCTCell().setS(fillGold);
                        break;
                    default:
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            c = (XSSFCell) r.createCell(cellnum++);
        }

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(cs);
        c.setCellValue(item.getNameEn());

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(csNumeric);
        c.setCellValue(item.getItemLevel());

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(cs);
        c.setCellValue(item.getCategory());

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(cs);
        c.setCellValue(item.getSubCategory());

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(csNumeric);
        c.setCellValue(item.getQuality());

        c = (XSSFCell) r.createCell(cellnum++);
        if (item.isController() && !item.isTank() && !item.isHealer()) {
            c.setCellStyle(csLightBlue);
            c.setCellValue(Role.Controller.toString());
        } else if (item.isTank() && !item.isController() && !item.isHealer()) {
            c.setCellStyle(csOrange);
            c.setCellValue(Role.Tank.toString());
        } else if (item.isHealer() && !item.isController() && !item.isTank()) {
            c.setCellStyle(csGreen);
            c.setCellValue(Role.Healer.toString());
        } else {
            c.setCellStyle(cs);
        }

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(cs);
        Alignment align = item.getAlignment();
        if (align != null) {
            c.setCellValue(align.toString());
        }

        c = (XSSFCell) r.createCell(cellnum++);
        c.setCellStyle(csNumeric);
        c.setCellValue(item.getId());
    }

    public void writeCharacters(Collection<Character> characters) {
        // Formula for Tank mitigation: =(Def*1.9)/(CR*3.5 + 46)

        Sheet sheet = wb.createSheet("Characters");
        Row r = null;
        XSSFCell c = null;

        int rownum = 0;
        r = sheet.createRow(rownum++);

        int cellnum = 0;
        String headers[] = { "Img.", "Name", "Level", "CR", "SP", "PvP CR", "Power", "Role", "Move", "Faction",
                "Personality", "Gender", "Origin", "Mentor", "Weapon", "Health", "Power", "Defense", "Tough", "Might",
                "Prec", "Rest", "Vit", "Dom", "Id" };

        for (String header : headers) {
            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs1);
            c.setCellValue(header);
        }

        for (Character character : characters) {
            CellStyle cellStyle = csVertCenter;

            r = sheet.createRow(rownum++);

            cellnum = 0;

            if (character.getId() > 0) {
                try {
                    new AddDimensionedImage().addImageToSheet(cellnum++, rownum - 1, sheet,
                            sheet.createDrawingPatriarch(), new File(character.getImageId() + ".png").toURI().toURL(),
                            24, 24, AddDimensionedImage.EXPAND_ROW_AND_COLUMN);
                } catch (IOException e) {
                }
            } else {
                c = (XSSFCell) r.createCell(cellnum++);
            }

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs1);
            c.setCellValue(character.getName());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cellStyle);
            c.setCellValue(character.getLevel());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs1);
            c.setCellValue(character.getCombatRating());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs1);
            c.setCellValue(character.getSkillPoints());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cellStyle);
            c.setCellValue(character.getPvpCombatRating());

            c = (XSSFCell) r.createCell(cellnum++);
            Power power = character.getPower();
            c.setCellStyle(csCenter);
            switch (power) {
            case Atomic:
                c.getCTCell().setS(fillAtomic);
                break;
            case Celestial:
                c.getCTCell().setS(fillCelestial);
                break;
            case Earth:
                c.getCTCell().setS(fillEarth);
                break;
            case Electricity:
                c.getCTCell().setS(fillElectricity);
                break;
            case Fire:
                c.getCTCell().setS(fillFire);
                break;
            case Gadgets:
                c.getCTCell().setS(fillGadgets);
                break;
            case Ice:
                c.getCTCell().setS(fillIce);
                break;
            case Light:
                switch (character.getAlignment()) {
                case Hero:
                    c.getCTCell().setS(fillGreenLight);
                    break;
                case Villain:
                    c.getCTCell().setS(fillYellowLight);
                    break;
                }
                break;
            case Mental:
                c.getCTCell().setS(fillMental);
                break;
            case Munitions:
                c.getCTCell().setS(fillMunitions);
                break;
            case Nature:
                c.getCTCell().setS(fillNature);
                break;
            case Quantum:
                c.getCTCell().setS(fillQuantum);
                break;
            case Rage:
                c.getCTCell().setS(fillRage);
                break;
            case Sorcery:
                c.getCTCell().setS(fillSorcery);
                break;
            case Water:
                c.setCellStyle(cellStyle);
                break;
            default:
                c.setCellStyle(cellStyle);
                break;
            }
            c.setCellValue(power.toString());

            c = (XSSFCell) r.createCell(cellnum++);
            Role role = character.getRole();
            switch (role) {
            case Controller:
                c.setCellStyle(csPaleBlueCenter);
                break;
            case Healer:
                c.setCellStyle(csGreenCenter);
                break;
            case Tank:
                c.setCellStyle(csOrangeCenter);
                break;
            default:
                break;
            }
            c.setCellValue(role.toString());

            c = (XSSFCell) r.createCell(cellnum++);
            MovementMode movement = character.getMovementMode();
            if (movement != null) {
                switch (movement) {
                case Flight:
                    c.setCellStyle(csGreenCenter);
                    break;
                case Acrobat:
                    c.setCellStyle(csYellowCenter);
                    break;
                case Speed:
                    c.setCellStyle(csRedCenter);
                    break;
                }
                c.setCellValue(movement.getName());
            }

            c = (XSSFCell) r.createCell(cellnum++);
            Alignment alignment = character.getAlignment();
            switch (alignment) {
            case Hero:
                c.setCellStyle(csGreenCenter);
                break;
            case Villain:
                c.setCellStyle(csRedCenter);
                break;
            default:
                c.setCellStyle(cs);
                break;
            }
            c.setCellValue(alignment.toString());

            c = (XSSFCell) r.createCell(cellnum++);
            Personality personality = character.getPersonality();
            switch (personality.getNameEn()) {
            case "Serious":
                c.setCellStyle(csPaleBlueCenter);
                break;
            case "Powerful":
                c.setCellStyle(csGreenCenter);
                break;
            case "Primal":
                c.setCellStyle(csRedCenter);
                break;
            case "Comical":
                c.setCellStyle(csOrangeCenter);
                break;
            case "Flirty":
                c.setCellStyle(csYellowCenter);
                break;
            default:
                c.setCellStyle(cs);
                break;
            }
            c.setCellValue(personality.getNameEn());

            c = (XSSFCell) r.createCell(cellnum++);
            Gender gender = character.getGender();
            if (gender != null) {
                switch (gender) {
                case male:
                    c.setCellStyle(csPaleBlueCenter);
                    break;
                case female:
                    c.setCellStyle(csRoseCenter);
                    break;
                default:
                    c.setCellStyle(cs);
                    break;
                }
                c.setCellValue(gender.toString());
            }

            c = (XSSFCell) r.createCell(cellnum++);
            Origin origin = character.getOrigin();
            switch (origin) {
            case Tech:
                c.setCellStyle(csPaleBlueCenter);
                break;
            case Meta:
                c.setCellStyle(csGreenCenter);
                break;
            case Magic:
                c.setCellStyle(csOrangeCenter);
                break;
            default:
                c.setCellStyle(cs);
                break;
            }
            c.setCellValue(origin.toString());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cellStyle);
            c.setCellValue(character.getMentor());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cellStyle);
            c.setCellValue(character.getWeapon().toString());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getMaxHealth());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getMaxPower());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getDefense());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getToughness());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getMight());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getPrecision());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getRestoration());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getVitalization());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(cs2);
            c.setCellValue(character.getDominance());

            c = (XSSFCell) r.createCell(cellnum++);
            c.setCellStyle(csNumeric);
            c.setCellValue(character.getId());
            // r.setHeightInPoints(13);
        }

        for (int i = 1; i < cellnum; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void writeUnlockableFeats(Character character, Collection<Feat> feats) {
        Sheet sheets[] = { wb.createSheet(character.getName()) };
        writeFeats(feats, sheets);
    }

    public void writeNeededFeats(Collection<Feat> feats) {
        Set<Feat> sortedFeats = new TreeSet<Feat>(new FeatCompletedComparator());
        sortedFeats.addAll(feats);
        writeFeats(sortedFeats, "Needed");
    }

    public void writeAllFeats(Collection<Feat> feats) {
        Set<Feat> sortedFeats = new TreeSet<Feat>(new FeatComparator());
        sortedFeats.addAll(feats);
        writeFeats(sortedFeats, "All");
    }

    private void writeFeats(Collection<Feat> sortedFeats, String type) {
        System.out.println("Writing " + type + " Feats Excel file.");

        Sheet sheets[] = { wb.createSheet(type), wb.createSheet("Villains " + type), wb.createSheet("Heroes " + type) };

        writeFeats(sortedFeats, sheets);

        System.out.println("Finished writing " + type + " Feats Excel file.");
    }

    private void writeFeats(Collection<Feat> sortedFeats, Sheet[] sheets) {
        Row r = null;
        Cell c = null;

        for (Sheet sheet : sheets) {
            int rownum = 0;
            r = sheet.createRow(rownum++);

            int cellnum = 0;

            String headers[] = { "Im", "Category", "SubCategory", "Name", "Description", "Reward", "Faction", "Role",
                    "Origin", "Move", "Completed", "Order1", "Order2", "Id" };

            for (String header : headers) {
                c = r.createCell(cellnum++);
                c.setCellStyle(cs1);
                c.setCellValue(header);
            }

            for (Feat feat : sortedFeats) {
                boolean isEpisodeFeat = false;
                boolean isStyleFeat = false;
                boolean isLegendsFeat = false;
                if (sheet == sheets[0] || (sheet == sheets[1] && feat.getAlignment() == Alignment.Villain)
                        || (sheet == sheets[2] && feat.getAlignment() == Alignment.Hero)) {
                    CellStyle cellStyle1 = cs;
                    CellStyle cellStyle2 = cs;
                    if ("Styles".equals(feat.getCategory()) || feat.getDescriptionEn().contains("style")) {
                        isStyleFeat = true;
                        cellStyle1 = csGreen;
                        cellStyle2 = csGreen;
                    } else if ("Dueling".equals(feat.getSubCategory())) {
                        cellStyle1 = csGrey;
                    } else if ("Player vs. Player".equals(feat.getCategory())) {
                        cellStyle1 = csRed;
                    } else if ("Episodes".equals(feat.getCategory())) {
                        isEpisodeFeat = true;
                        if (("The Last Laugh".equals(feat.getSubCategory())
                                && (!feat.getDescriptionEn().toLowerCase().contains("duo")
                                        && !feat.getNameEn().startsWith("School of Hard")))
                                || ("Home Turf".equals(feat.getSubCategory())
                                        && feat.getDescriptionEn().contains("PvP"))) {
                            cellStyle1 = csRed;
                        } else {
                            cellStyle1 = csOrange;
                            cellStyle2 = csOrange;
                        }
                    }

                    r = sheet.createRow(rownum++);

                    cellnum = 0;

                    if (feat.getIconId() > 0) {
                        try {
                            new AddDimensionedImage().addImageToSheet(cellnum++, rownum - 1, sheet,
                                    sheet.createDrawingPatriarch(),
                                    new File("Icon" + feat.getIconId() + ".png").toURI().toURL(), 24, 24,
                                    AddDimensionedImage.EXPAND_ROW_AND_COLUMN);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        c = r.createCell(cellnum++);
                    }

                    c = r.createCell(cellnum++);
                    String category = feat.getCategory();
                    if ("Legends PvE".equals(category)) {
                        if ("".equals(feat.getSubCategory())) {
                            isLegendsFeat = true;
                        }
                        c.setCellStyle(csPaleBlue);
                    } else {
                        c.setCellStyle(cellStyle2);
                    }
                    c.setCellValue(category);

                    c = r.createCell(cellnum++);
                    if ("Legends PvE".equals(category)) {
                        c.setCellStyle(csPaleBlue);
                    } else {
                        c.setCellStyle(cellStyle1);
                    }
                    c.setCellValue(feat.getSubCategory());

                    c = r.createCell(cellnum++);
                    c.setCellStyle(cellStyle2);
                    XSSFRichTextString rts = new XSSFRichTextString(feat.getNameEn());
                    rts.applyFont(fBold);
                    c.setCellValue(rts);

                    c = r.createCell(cellnum++);
                    c.setCellStyle(cellStyle2);
                    if (isEpisodeFeat) {
                        formatEpisodeFeat(c, feat, fBold);
                    } else if (isStyleFeat) {
                        formatStyleFeat(c, feat, fBold);
                    } else if (isLegendsFeat) {
                        formatLegendsFeat(c, feat, fBold);
                    } else {
                        c.setCellValue(feat.getDescriptionEn());
                    }

                    c = r.createCell(cellnum++);
                    long reward = feat.getReward();
                    if (reward == 10) {
                        c.setCellStyle(csGreen);
                    } else if (reward == 25) {
                        c.setCellStyle(csYellow);
                    } else if (reward == 50) {
                        c.setCellStyle(csRed);
                    } else {
                        c.setCellStyle(cs);
                    }
                    c.setCellValue(reward);

                    c = r.createCell(cellnum++);
                    Alignment alignment = feat.getAlignment();
                    if (alignment != null) {
                        switch (alignment) {
                        case Hero:
                            c.setCellStyle(csGreen);
                            break;
                        case Villain:
                            c.setCellStyle(csRed);
                            break;
                        default:
                            c.setCellStyle(cs);
                        }
                        c.setCellValue(alignment.toString());
                    }

                    c = r.createCell(cellnum++);
                    String role = feat.getRole();
                    if ("Controller".equals(role)) {
                        c.setCellStyle(csPaleBlue);
                    } else if ("Healer".equals(role)) {
                        c.setCellStyle(csGreen);
                    } else if ("Tank".equals(role)) {
                        c.setCellStyle(csOrange);
                    } else {
                        c.setCellStyle(cs);
                    }
                    c.setCellValue(role);

                    c = r.createCell(cellnum++);
                    Origin origin = feat.getOrigin();
                    if (origin != null) {
                        switch (origin) {
                        case Tech:
                            c.setCellStyle(csPaleBlue);
                            break;
                        case Meta:
                            c.setCellStyle(csGreen);
                            break;
                        case Magic:
                            c.setCellStyle(csOrange);
                            break;
                        default:
                            c.setCellStyle(cs);
                            break;
                        }
                        c.setCellValue(origin.toString());
                    }

                    c = r.createCell(cellnum++);
                    MovementMode movement = feat.getMovementMode();
                    if (movement != null) {
                        switch (movement) {
                        case Flight:
                            c.setCellStyle(csGreen);
                            break;
                        case Acrobat:
                            c.setCellStyle(csYellow);
                            break;
                        case Speed:
                            c.setCellStyle(csRed);
                            break;
                        default:
                            c.setCellStyle(cs);
                            break;
                        }
                        c.setCellValue(movement.toString());
                    }

                    c = r.createCell(cellnum++);
                    c.setCellStyle(cs2);
                    c.setCellValue(feat.getCompleted());

                    c = r.createCell(cellnum++);
                    c.setCellStyle(csHide);
                    c.setCellValue(feat.getOrder1());

                    c = r.createCell(cellnum++);
                    c.setCellStyle(csHide);
                    c.setCellValue(feat.getOrder2());

                    c = r.createCell(cellnum++);
                    c.setCellStyle(csHide);
                    c.setCellValue(feat.getId());

                    r.setHeightInPoints(15);
                }
            }

            for (int i = 1; i < cellnum; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    private static void formatLegendsFeat(Cell c, Feat feat, Font font) {
        String description = feat.getDescriptionEn();
        XSSFRichTextString rts = new XSSFRichTextString(description);

        int start = -1;
        for (int i = 0; i < LEGENDS_STARTS.length; i++) {
            String legendsStart = LEGENDS_STARTS[i];
            start = description.indexOf(legendsStart);
            if (start >= 0) {
                start += legendsStart.length();
                int end = -1;
                if ((end = description.indexOf(LEGENDS_ENDS[i], start + 1)) > start) {
                    // End found.
                } else {
                    end = description.length();
                }
                rts.applyFont(start, end, font);
                break;
            }
        }

        c.setCellValue(rts);
    }

    private static void formatStyleFeat(Cell c, Feat feat, Font font) {
        String description = feat.getDescriptionEn();
        XSSFRichTextString rts = new XSSFRichTextString(description);

        int start = -1;
        for (String styleStart : STYLE_STARTS) {
            start = description.indexOf(styleStart);
            if (start >= 0) {
                start += styleStart.length();
                int end = -1;
                if ((end = description.indexOf(STYLE_END, start + 1)) > start
                        || (end = description.indexOf(STYLE_END2, start + 1)) > start) {
                    // End found.
                } else {
                    end = description.length();
                }
                rts.applyFont(start, end, font);
                break;
            }
        }

        c.setCellValue(rts);
    }

    private static void formatEpisodeFeat(Cell c, Feat feat, Font font) {
        String description = feat.getDescriptionEn();
        XSSFRichTextString rts = new XSSFRichTextString(description);

        int start = -1;
        int end = -1;
        try {
            Episode ep = Episodes.getInstance().getEpisode(feat.getOrder2());
            if (ep != null) {
                for (String mission : ep.getMissions()) {
                    applyFont(font, description, rts, mission);
                }
            } else if (feat.getSubCategory().contains("&")) {
                String names[] = feat.getSubCategory().split("&");
                List<String> missions = new ArrayList<String>();
                for (String name : names) {
                    missions.add(name.trim());
                }
                missions.add("(Elite)");
                missions.add("(Non-Elite)");
                switch (feat.getOrder2()) {
                case 140:
                    missions.add("DC Bombshells Posters");
                    break;
                case 150:
                    missions.add("Corrupted T'Larto");
                    break;
                case 180:
                    missions.add("Fonts of Gluttony");
                    missions.add("Nanda Parbat");
                    break;
                default:
                    break;
                }
                for (String mission : missions) {
                    if (mission.startsWith("The ")) {
                        mission = mission.substring(4);
                    }
                    applyFont(font, description, rts, mission);
                }
            } else {
                if ((start = description.indexOf(EPISODE_START)) >= 0) {
                    start += EPISODE_START.length();
                } else if ((start = description.indexOf(EPISODE_START2)) >= 0) {
                    start += EPISODE_START2.length();
                } else if ((start = description.indexOf(EPISODE_START3)) >= 0) {
                    start += EPISODE_START3.length();
                } else if ((start = description.indexOf(EPISODE_START4)) >= 0) {
                    start += EPISODE_START4.length();
                }

                if (start >= 0) {
                    end = description.length();
                    if ((end = description.indexOf(EPISODE_SOLO, start + 1)) > start
                            || (end = description.indexOf(EPISODE_OP, start + 1)) > start
                            || (end = description.indexOf(EPISODE_OP2, start + 1)) > start
                            || (end = description.indexOf(EPISODE_ALERT, start + 1)) > start
                            || (end = description.indexOf(EPISODE_ALERT2, start + 1)) > start
                            || (end = description.indexOf(EPISODE_RAID, start + 1)) > start
                            || (end = description.indexOf(EPISODE_MISSION, start + 1)) > start
                            || (end = description.indexOf(EPISODE_DUO, start + 1)) > start
                            || (end = description.indexOf(EPISODE_DUO2, start + 1)) > start) {
                        rts.applyFont(start, end, font);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Caught exception formatting " + feat.getNameEn() + ": " + feat.getDescriptionEn());
            e.printStackTrace();
        }
        c.setCellValue(rts);
    }

    private static void applyFont(Font font, String description, XSSFRichTextString rts, String mission) {
        int start = description.indexOf(mission);
        if (start >= 0) {
            int end = start + mission.length() + 1;
            if (end > rts.length()) {
                end = rts.length();
            }
            rts.applyFont(start, end, font);
        }
    }
}
