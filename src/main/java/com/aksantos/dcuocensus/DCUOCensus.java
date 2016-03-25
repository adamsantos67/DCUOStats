package com.aksantos.dcuocensus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.Item;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.EquipmentSlot;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Role;

/**
 * Hello world!
 *
 */
public class DCUOCensus {
    private static final int MIN_CR = 100;

    private static final Logger logger = LogManager.getLogger(DCUOCensus.class);

    private static final String CHARACTERS_INI = "characters.ini";
    private static final String ALL_FEATS_SER = "allFeats.ser";
    private static final String ALL_ITEMS_SER = "allItems.ser";
    private static final Long JAILBIRD_FEAT_ID = 959767l;
    private static final String imageDir = "images/";

    private static final DCUOCensusClient censusClient = new DCUOCensusJsonClient();

    private static final String delim = "\t";

    private Map<Long, Item> items = null;

    public static void main(String[] args) {
        DCUOCensus app = new DCUOCensus();
        app.process();
        logger.info("Done!");
    }

    private void process() {
        XLSXWriter xlsxWriter = new XLSXWriter(imageDir);

        try {
            processItems();

            Set<Character> sortedCharacters = processCharacters(xlsxWriter);

            Map<Long, Feat> feats = processFeats();

            Map<Long, Feat> featsNeeded = initializeNeededFeats(feats);

            Set<Long> featsCompleted = new HashSet<Long>();

            findNeededAndCompletedFeats(sortedCharacters, feats, featsNeeded, featsCompleted);

            getCompletedCounts(feats);

            xlsxWriter.writeAllFeats(feats.values());

            xlsxWriter.writeNeededFeats(featsNeeded.values());

            processUnlockableFeats(sortedCharacters, feats, featsCompleted, xlsxWriter);

            logger.info("Total Feats: " + feats.size());
            logger.info("Needed Feats: " + featsNeeded.size());
            logger.info((feats.size() - featsNeeded.size()) * 100 / feats.size() + "% complete.");
        } catch (Exception e) {
            logger.error("Exception: " + e, e);
        } finally {
            xlsxWriter.writeFile();
        }
    }

    private void processUnlockableFeats(Set<Character> sortedCharacters, Map<Long, Feat> feats,
            Set<Long> featsCompleted, XLSXWriter xlsxWriter) {
        try {
            for (Character character : sortedCharacters) {
                if (character.getCombatRating() > MIN_CR) {
                    for (Long completedFeatId : featsCompleted) {
                        if (!character.getCompletedFeats().contains(completedFeatId)) {
                            Feat completedFeat = feats.get(completedFeatId);
                            if (completedFeat != null) {
                                Alignment featFaction = completedFeat.getAlignment();
                                if (featFaction == null || featFaction == character.getAlignment()) {
                                    Origin featOrigin = completedFeat.getOrigin();
                                    if (featOrigin == null || featOrigin == character.getOrigin()) {
                                        MovementMode featMove = completedFeat.getMovementMode();
                                        if (featMove == null || featMove == character.getMovementMode()) {
                                            List<Role> featRoles = completedFeat.getRoles();
                                            if (featRoles == null || featRoles.isEmpty()
                                                    || featRoles.contains(character.getRole())) {
                                                character.getUnlockableFeats().add(completedFeatId);
                                            } else {
                                                for (Role role : Role.values()) {
                                                    if (featRoles.contains(role)) {
                                                        character.addUnlockableRoleFeat(role, completedFeatId);
                                                    }
                                                }
                                            }
                                        } else {
                                            for (MovementMode move : MovementMode.values()) {
                                                if (featMove == move) {
                                                    character.addUnlockabeMovementFeat(move, completedFeatId);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                character.getUnlockableFeats().add(completedFeatId);
                            }
                        }
                    }
                }

                if (character.getCombatRating() > MIN_CR) {
                    Set<Feat> sortedFeats = sortFeatIdSet(feats, character.getUnlockableFeats());

                    Map<MovementMode, Collection<Feat>> movementFeats = getFeatEnumMap(MovementMode.values(), feats,
                            character.getUnlockableMovementFeats());

                    Map<Role, Collection<Feat>> roleFeats = getFeatEnumMap(Role.values(), feats,
                            character.getUnlockableRoleFeats());

                    xlsxWriter.writeUnlockableFeats(character, sortedFeats, roleFeats, movementFeats);
                }
            }
        } catch (Exception e) {
            logger.error("Exception: " + e, e);
        }
    }

    private <E extends Enum<E>> Map<E, Collection<Feat>> getFeatEnumMap(E[] values, Map<Long, Feat> feats,
            Map<E, Set<Long>> featIdMap) {
        Map<E, Collection<Feat>> featMap = new TreeMap<E, Collection<Feat>>();
        for (E value : values) {
            Set<Feat> sortedFeats = sortFeatIdSet(feats, featIdMap.get(value));
            if (sortedFeats != null && !sortedFeats.isEmpty()) {
                featMap.put(value, sortedFeats);
            }
        }
        return featMap;
    }

    private Set<Feat> sortFeatIdSet(Map<Long, Feat> feats, Set<Long> featIds) {
        Set<Feat> sortedFeats = new TreeSet<Feat>(FeatComparator.getInstance());
        if (featIds != null && !featIds.isEmpty()) {
            for (Long featId : featIds) {
                Feat feat = feats.get(featId);
                if (feat != null) {
                    sortedFeats.add(feat);
                } else {
                    Feat missingFeat = new Feat();
                    missingFeat.setId(featId);
                    sortedFeats.add(missingFeat);
                }
            }
        }
        return sortedFeats;
    }

    private void findNeededAndCompletedFeats(Set<Character> sortedCharacters, Map<Long, Feat> feats,
            Map<Long, Feat> featsNeeded, Set<Long> featsCompleted) {
        for (Character character : sortedCharacters) {
            try {
                Set<Long> featIds = censusClient.getCharacterFeats(character);
                character.setCompletedFeats(featIds);

                for (Long featId : featIds) {
                    featsNeeded.remove(featId);
                    if (!feats.containsKey(featId)) {
                        Feat newFeat = new Feat();
                        newFeat.setId(featId);
                        feats.put(featId, newFeat);
                    }

                    featsCompleted.add(featId);
                }

            } catch (Exception e) {
                logger.error("Exception for " + character.getName() + ": " + e, e);
            }
        }
    }

    private Map<Long, Feat> initializeNeededFeats(Map<Long, Feat> feats) {
        Map<Long, Feat> featsNeeded = new TreeMap<Long, Feat>(feats);
        // Remove un-obtainable feats.
        featsNeeded.remove(1264271l);
        featsNeeded.remove(2646055l);
        featsNeeded.remove(2646053l);
        return featsNeeded;
    }

    private Map<Long, Feat> processFeats() throws IOException, DCUOException {
        Map<Long, Feat> feats = loadFeats();

        if (feats == null) {
            feats = censusClient.getFeats();
        }
        return feats;
    }

    private void processItems() throws DCUOException {
        items = loadItems();

        if (items == null) {
            items = censusClient.getItems();
        }
    }

    private static Map<Long, Feat> loadFeats() {
        return loadSerializedMap(ALL_FEATS_SER);
    }

    private static Map<Long, Item> loadItems() {
        return loadSerializedMap(ALL_ITEMS_SER);
    }

    @SuppressWarnings("unchecked")
    private static <T> Map<Long, T> loadSerializedMap(String filename) {
        Map<Long, T> map = null;
        ObjectInputStream objStreamIn = null;
        try {
            File file = new File(filename);
            long now = System.currentTimeMillis();
            logger.debug("Now: " + now);
            logger.debug("Mod: " + file.lastModified());
            if (file.lastModified() > (now - 1000 * 60 * 60 * 24)) {
                InputStream streamIn = new FileInputStream(filename);
                objStreamIn = new ObjectInputStream(streamIn);
                Object obj = objStreamIn.readObject();
                if (obj instanceof Map<?, ?>) {
                    map = (Map<Long, T>) obj;
                }
            }
        } catch (ClassNotFoundException e) {
            logger.warn(filename + " is incompatible with this version. Reading items from DCUO.");
        } catch (FileNotFoundException fnfe) {
            logger.warn(filename + " file not found. Reading items from DCUO.");
        } catch (InvalidClassException fnfe) {
            logger.warn(filename + " is incompatible with this version. Reading items from DCUO.");
        } catch (IOException ioe) {
            logger.error("Exception: " + ioe, ioe);
        } finally {
            if (objStreamIn != null) {
                try {
                    objStreamIn.close();
                } catch (IOException ioe) {
                }
            }
        }
        return map;
    }

    private Set<Character> processCharacters(XLSXWriter xlsxWriter)
            throws UnsupportedEncodingException, IOException, DCUOException {
        Map<Long, Character> characters = new TreeMap<Long, Character>();

        Set<String> characterNames = readCharacterNames();
        for (String characterName : characterNames) {
            Character character = censusClient.getCharacter(characterName);
            characters.put(character.getId(), character);
        }

        Set<Character> sortedCharacters = new TreeSet<Character>(new CharacterComparator());
        sortedCharacters.addAll(characters.values());

        logger.info("id" + delim + "name" + delim + "level" + delim + "CR" + delim + "SP" + delim + "Power" + delim
                + "Movement" + delim + "Faction" + delim + "Personality" + delim + "Gender" + delim + "Origin");
        for (Character character : sortedCharacters) {
            logger.info(character.getId() + delim + character.getName() + delim + character.getLevel() + delim
                    + character.getCombatRating() + delim + character.getSkillPoints() + delim + character.getPower()
                    + delim + character.getMovementMode() + delim + character.getAlignment() + delim
                    + character.getPersonality().getNameEn() + delim + character.getGender() + delim
                    + character.getOrigin());

            censusClient.saveImage(character);
            // showImage(character.getId());

            getCharacterItems(character);
        }
        saveItems();

        xlsxWriter.writeCharacters(sortedCharacters);
        xlsxWriter.writeCharacterItems(sortedCharacters, items);
        return sortedCharacters;
    }

    private void getCharacterItems(Character character) throws IOException, DCUOException {
        List<CharactersItem> charItems = censusClient.getCharacterItems(character);
        character.setCharacterItems(charItems);

        logger.debug(character.getName() + " has " + charItems.size() + " items.");
        for (CharactersItem charItem : charItems) {
            Item item = null;
            if (items != null) {
                item = items.get(charItem.getItemId());
            }
            if (item == null) {
                item = censusClient.getItem(charItem.getItemId());
                if (item != null && items != null) {
                    items.put(item.getId(), item);
                }
            }
            if (item != null) {
                censusClient.saveIcon(item);
                logger.debug(EquipmentSlot.getById(charItem.getEquipmentSlotId()) + delim + item.getNameEn() + delim
                        + item.getItemLevel());
            } else {
                logger.debug(EquipmentSlot.getById(charItem.getEquipmentSlotId()) + delim + charItem.getItemId());
            }
        }
    }

    private void getCompletedCounts(Map<Long, Feat> feats) {
        logger.info("Getting completed feat counts.");

        Feat firstFeat = feats.get(JAILBIRD_FEAT_ID);
        if (firstFeat == null || firstFeat.getCompleted() == 0) {

            for (Feat feat : feats.values()) {
                feat.setCompleted(getFeatCompletedCount(feat));

                censusClient.saveIcon(feat);
            }

            saveFeats(feats);
        } else {
            logger.info("Feat completed counts already loaded.");
        }
        logger.info("Finished getting completed feat counts.");
    }

    private static void saveFeats(Map<Long, Feat> feats) {
        ObjectOutputStream objOutStream = null;
        try {
            FileOutputStream fout = new FileOutputStream(ALL_FEATS_SER);
            objOutStream = new ObjectOutputStream(fout);
            objOutStream.writeObject(feats);
        } catch (IOException e) {
            logger.error("Exception: " + e, e);
        } finally {
            if (objOutStream != null) {
                try {
                    objOutStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void saveItems() {
        ObjectOutputStream objOutStream = null;
        try {
            FileOutputStream fout = new FileOutputStream(ALL_ITEMS_SER);
            objOutStream = new ObjectOutputStream(fout);
            objOutStream.writeObject(items);
        } catch (IOException e) {
            logger.error("Exception: " + e, e);
        } finally {
            if (objOutStream != null) {
                try {
                    objOutStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static long getFeatCompletedCount(Feat feat) {
        long count = 0;
        try {
            count = censusClient.getFeatCompletedCount(feat);
        } catch (DCUOException e) {
            logger.error(e.getMessage(), e);
        }
        return count;
    }

    private static Set<String> readCharacterNames() {
        Set<String> names = new HashSet<String>();
        File file = new File(CHARACTERS_INI);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    names.add(line);
                }
            }
        } catch (IOException e) {
            logger.error("Exception: " + e, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return names;
    }
}
