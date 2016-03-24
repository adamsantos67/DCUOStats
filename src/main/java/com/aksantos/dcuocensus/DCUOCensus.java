package com.aksantos.dcuocensus;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.FeatCategory;
import com.aksantos.dcuocensus.models.Item;
import com.aksantos.dcuocensus.models.ItemCategory;
import com.aksantos.dcuocensus.models.Name;
import com.aksantos.dcuocensus.models.Personality;
import com.aksantos.dcuocensus.models.Reward;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.EquipmentSlot;
import com.aksantos.dcuocensus.models.enums.Gender;
import com.aksantos.dcuocensus.models.enums.IconId;
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
    private static final String serviceHost = "http://census.daybreakgames.com";
    private static final String serviceUrl = serviceHost + "/s:BluesStats/get/dcuo:v1/";
    private static final String rewardUrl = serviceUrl + "feat_reward?c:limit=100";
    private static final String featsUrl = serviceUrl + "feat?c:limit=10000";
    private static final String itemsUrl = serviceUrl + "item?c:limit=100";
    private static final String itemByIdUrl = serviceUrl + "item?item_id=";
    private static final String featCategoriesUrl = serviceUrl + "feat_category?c:limit=1000";
    private static final String itemCategoriesUrl = serviceUrl + "item_category?c:limit=1000";
    private static final String personalityUrl = serviceUrl + "personality?c:limit=1000";
    private static final String characterUrl = serviceUrl + "character?world_id=2&name=";
    private static final String characterIdMapUrl = serviceUrl + "char_id_mapping?new_character_id=";
    private static final String characterItemsUrl = serviceUrl + "characters_item?c:limit=1000&character_id=";

    private static final String characterQuery = "&c:lang=en";

    private static final String characterFeatsUrl = serviceUrl
            + "characters_completed_feat/?c:limit=10000&character_id=";

    private static final String serviceCountUrl = serviceHost + "/s:BluesStats/count/dcuo:v1/";
    private static final String characterFeatCountUrl = serviceCountUrl + "characters_completed_feat?feat_id=";

    private static final String imageUrl = serviceHost + "/files/dcuo/images/character/paperdoll/";
    private static final String imageUrl2 = ".png?fallback=dcuo.paperdoll.";
    private static final String imageDir = "images/";
    private static final CensusParser parser = new CensusParser();

    private static final String delim = "\t";

    private Map<Long, Alignment> alignments = null;
    private Map<Long, ItemCategory> itemCategories = null;
    private Map<Long, Item> items = null;
    private final Set<Long> iconIds = new TreeSet<Long>();

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

            List<Long> featsCompleted = new ArrayList<Long>();

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
            List<Long> featsCompleted, XLSXWriter xlsxWriter) {
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

                    // Map<MovementMode, Collection<Feat>> movementFeats =
                    // getFeatEnumMap(MovementMode.values(), feats,
                    // character.getUnlockableMovementFeats());

                    Map<MovementMode, Collection<Feat>> movementFeats = new TreeMap<MovementMode, Collection<Feat>>();
                    for (MovementMode move : MovementMode.values()) {
                        Set<Feat> sortedMovementFeats = sortFeatIdSet(feats,
                                character.getUnlockableMovementFeatSet(move));
                        if (sortedMovementFeats != null && !sortedMovementFeats.isEmpty()) {
                            movementFeats.put(move, sortedMovementFeats);
                        }
                    }

                    // Map<Role, Collection<Feat>> roleFeats =
                    // getFeatEnumMap(Role.values(), feats,
                    // character.getUnlockableRoleFeats());

                    Map<Role, Collection<Feat>> roleFeats = new TreeMap<Role, Collection<Feat>>();
                    for (Role role : Role.values()) {
                        Set<Feat> sortedRoleFeats = sortFeatIdSet(feats, character.getUnlockableRoleFeatSet(role));
                        if (sortedRoleFeats != null && !sortedRoleFeats.isEmpty()) {
                            roleFeats.put(role, sortedRoleFeats);
                        }
                    }

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
            Map<Long, Feat> featsNeeded, List<Long> featsCompleted) {
        for (Character character : sortedCharacters) {
            try {
                Set<Long> featIds = parser.parseCharacterFeats(new URL(characterFeatsUrl + character.getId()),
                        character.getName());
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

    private Map<Long, Feat> processFeats() throws IOException {
        Map<Long, Feat> feats = loadFeats();

        if (feats == null) {
            Map<Long, FeatCategory> featCategories = parser.parseFeatCategories(new URL(featCategoriesUrl));

            Map<Long, Reward> rewards = parser.parseRewards(new URL(rewardUrl));

            feats = parser.parseFeats(new URL(featsUrl), featCategories, rewards, alignments);

            addMissingFeats(feats);
        }
        return feats;
    }

    private void processItems() throws IOException {
        itemCategories = parser.parseItemCategories(new URL(itemCategoriesUrl));

        items = loadItems();

        if (items == null) {
            items = parser.parseItems(new URL(itemsUrl), itemCategories);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Long, Feat> loadFeats() {
        Map<Long, Feat> feats = null;
        ObjectInputStream objStreamIn = null;
        try {
            File file = new File(ALL_FEATS_SER);
            long now = System.currentTimeMillis();
            logger.debug("Now: " + now);
            logger.debug("Mod: " + file.lastModified());
            if (file.lastModified() > (now - 1000 * 60 * 60 * 24)) {
                InputStream streamIn = new FileInputStream(ALL_FEATS_SER);
                objStreamIn = new ObjectInputStream(streamIn);
                Object obj = objStreamIn.readObject();
                if (obj instanceof Map<?, ?>) {
                    feats = (Map<Long, Feat>) obj;
                }
            }
        } catch (ClassNotFoundException e) {
            logger.warn(ALL_FEATS_SER + " is incompatible with this version. Reading feats from DCUO.");
        } catch (FileNotFoundException fnfe) {
            logger.warn(ALL_FEATS_SER + " file not found. Reading feats from DCUO.");
        } catch (InvalidClassException fnfe) {
            logger.warn(ALL_FEATS_SER + " is incompatible with this version. Reading feats from DCUO.");
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
        return feats;
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

    private Set<Character> processCharacters(XLSXWriter xlsxWriter) throws UnsupportedEncodingException, IOException {
        Map<Long, Character> characters = new TreeMap<Long, Character>();

        Map<Long, Personality> personalities = parser.parsePersonalities(new URL(personalityUrl));

        Set<String> characterNames = readCharacterNames();
        for (String characterName : characterNames) {
            String encodedName = URLEncoder.encode(characterName, "UTF-8");
            Character character = parser.parseCharacters(new URL(characterUrl + encodedName + characterQuery),
                    personalities, alignments);
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

            character.setImageId(saveImage(character.getId(), character.getGender()));
            // showImage(character.getId());

            getCharacterItems(character);
        }
        saveItems();

        xlsxWriter.writeCharacters(sortedCharacters);
        xlsxWriter.writeCharacterItems(sortedCharacters, items);
        return sortedCharacters;
    }

    private void getCharacterItems(Character character) throws IOException {
        List<CharactersItem> charItems = parser.parseCharacterItems(new URL(characterItemsUrl + character.getId()));
        character.setCharacterItems(charItems);

        logger.debug(character.getName() + " has " + charItems.size() + " items.");
        for (CharactersItem charItem : charItems) {
            Item item = null;
            if (items != null) {
                item = items.get(charItem.getItemId());
            }
            if (item == null) {
                item = getItem(charItem.getItemId());
                if (item != null && items != null) {
                    items.put(item.getId(), item);
                }
            }
            if (item != null) {
                getIcon(item.getIconId(), item.getImagePath(), item.getCategory(), item.getSubCategory());
                logger.debug(EquipmentSlot.getById(charItem.getEquipmentSlotId()) + delim + item.getNameEn() + delim
                        + item.getItemLevel());
            } else {
                logger.debug(EquipmentSlot.getById(charItem.getEquipmentSlotId()) + delim + charItem.getItemId());
            }
        }
    }

    private Item getItem(long id) throws IOException {
        Item item = null;
        Map<Long, Item> items = parser.parseItems(new URL(itemByIdUrl + id), itemCategories);
        if (items != null && !items.isEmpty()) {
            item = items.get(id);
        }
        return item;
    }

    private void getCompletedCounts(Map<Long, Feat> feats) {
        logger.info("Getting completed feat counts.");

        Feat firstFeat = feats.get(JAILBIRD_FEAT_ID);
        if (firstFeat == null || firstFeat.getCompleted() == 0) {

            for (Feat feat : feats.values()) {
                feat.setCompleted(getFeatCompletedCount(feat));

                getIcon(feat.getIconId(), feat.getImagePath(), "Feat", "Icon");
            }

            saveFeats(feats);
        } else {
            logger.info("Feat completed counts already loaded.");
        }
        logger.info("Finished getting completed feat counts.");
    }

    private void getIcon(long iconId, String path, String category, String subCategory) {
        if (iconId > 0) {
            if (!iconIds.contains(iconId)) {
                iconIds.add(iconId);
            }
            saveIcon(iconId, path, category, subCategory);
        }
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
            // String out = HtmlReader.getHTML(characterFeatCountUrl +
            // feat.getId(), feat.getNameEn());
            count = parser.parseCount(new URL(characterFeatCountUrl + feat.getId()), feat.getNameEn());
        } catch (Exception e) {
            logger.error("Failed due to exception getting completed count for " + feat.getNameEn(), e);
        }
        return count;
    }

    private void addMissingFeats(Map<Long, Feat> feats) {
        List<Role> tankRole = new ArrayList<Role>();
        tankRole.add(Role.Tank);
        addFeat(feats, 713944, "General", "", 10, 0, "Tanks Very Much", "Achieve level 10 and gain the Tank Role", 10,
                IconId.GENERAL, null, null, null, tankRole);
        List<Role> controllerRole = new ArrayList<Role>();
        controllerRole.add(Role.Controller);
        addFeat(feats, 713945, "General", "", 10, 0, "Mission Control", "Achieve level 10 and gain the Controller Role",
                10, IconId.GENERAL, null, null, null, controllerRole);
        List<Role> healerRole = new ArrayList<Role>();
        healerRole.add(Role.Healer);
        addFeat(feats, 926019, "General", "", 10, 0, "The Healing Touch", "Achieve level 10 and gain the Healer Role",
                10, IconId.GENERAL, null, null, null, healerRole);

        addFeat(feats, 715081, "General", "", 10, 0, "Knight for Justice",
                "Achieve level 30 as a hero mentored by Batman", 25, IconId.GENERAL, Alignment.Hero, null, Origin.Tech);
        addFeat(feats, 715082, "General", "", 10, 0, "Warrior of Truth",
                "Achieve level 30 as a hero mentored by Wonder Woman", 25, IconId.GENERAL, Alignment.Hero, null,
                Origin.Magic);
        addFeat(feats, 715083, "General", "", 10, 0, "Champion of Earth",
                "Achieve level 30 as a hero mentored by Superman", 25, IconId.GENERAL, Alignment.Hero, null,
                Origin.Meta);

        addFeat(feats, 938050, "General", "", 10, 0, "The Queen's Favorite",
                "Achieve level 30 as a villain mentored by Circe", 25, IconId.GENERAL, Alignment.Villain, null,
                Origin.Magic);
        addFeat(feats, 938051, "General", "", 10, 0, "Criminal Mastermind",
                "Achieve level 30 as a villain mentored by Lex Luthor", 25, IconId.GENERAL, Alignment.Villain, null,
                Origin.Meta);
        addFeat(feats, 938052, "General", "", 10, 0, "The Joker in the Deck",
                "Achieve level 30 as a villain mentored by the Joker", 25, IconId.GENERAL, Alignment.Villain, null,
                Origin.Tech);

        addFeat(feats, 715084, "General", "", 10, 0, "Agile Ace", "Achieve level 30 with an Agile movement mode", 25,
                IconId.GENERAL, null, MovementMode.Acrobat);
        addFeat(feats, 715085, "General", "", 10, 0, "Aerial Antics", "Achieve level 30 with an Aerial movement mode",
                25, IconId.GENERAL, null, MovementMode.Flight);
        addFeat(feats, 715086, "General", "", 10, 0, "Swiftly Supreme", "Achieve level 30 with a Swift movement mode",
                25, IconId.GENERAL, null, MovementMode.Speed);
        addFeat(feats, 959776, "General", "", 10, 0, "Master Escape Artist", "Use the Breakout ability 100 times", 10,
                IconId.GENERAL);

        addWTTeamUp(feats, 2970885, "Superman & Batman");
        addWTTeamUp(feats, 2970892, "Power Girl & Donna Troy");
        addWTTeamUp(feats, 2970899, "Steel & Supergirl");
        addWTTeamUp(feats, 2970906, "The Huntress & Guy Gardner");
        addWTTeamUpChecklist(feats, 2970913, Alignment.Hero);
        addWTChecklist(feats, 2970930, Alignment.Hero);

        addWTTeamUp(feats, 2970950, "General Zod & Ursa");
        addWTTeamUp(feats, 2970957, "The Joker & Lex Luthor");
        addWTTeamUp(feats, 2970964, "Catwoman & Cheetah");
        addWTTeamUp(feats, 2970971, "Bane & Bizarro");
        addWTTeamUpChecklist(feats, 2970978, Alignment.Villain);
        addWTChecklist(feats, 2970995, Alignment.Villain);

        addOanFeat(feats, 3069406, "Legends PvE: Conviction", "Complete Oan Sciencells without being knocked out", 25);
        addOanFeat(feats, 3069419, "Legends PvE: Faster Than Light Waves",
                "Complete Oan Sciencells in 20 minutes or less", 25);
        addOanFeat(feats, 3069425, "Legends PvE: Parole Denied",
                "Defeat Red Lantern Vice, Yellow Lantern Lyssa Drak and Evil Star in the Oan Sciencells", 25,
                Alignment.Hero);
        addOanFeat(feats, 3069442, "Legends PvE: Parole Granted",
                "Defeat Red Lantern Vice, Green Lantern Guy Gardner and Evil Star in the Oan Sciencells", 25,
                Alignment.Villain);

        addOanTeamUp(feats, 3069463, "Batman & Robin");
        addOanTeamUp(feats, 3069470, "John Stewart & Kyle Raynor");
        addOanTeamUp(feats, 3069477, "Hal Jordan & Saint Walker");
        addOanTeamUp(feats, 3069484, "Nightwing & Kilowog");
        addOanTeamUpChecklist(feats, 3069491, Alignment.Hero);
        addOanChecklist(feats, 3069508, Alignment.Hero);

        addOanTeamUp(feats, 3069528, "Black Adam & Circe");
        addOanTeamUp(feats, 3069535, "Cheetah & Lex Luthor");
        addOanTeamUp(feats, 3069542, "Harley Quinn & Arkillo");
        addOanTeamUp(feats, 3069549, "The Joker & Atrocitus");
        addOanTeamUpChecklist(feats, 3069556, Alignment.Villain);
        addOanChecklist(feats, 3069573, Alignment.Villain);

        addFeat(feats, 963630, "General", "", 10, 0, "Red Barrel Buster Rookie", "Break 5 red Explosion barrels", 10,
                IconId.ALERTS);
        addFeat(feats, 938806, "Styles", "", 40, 0, "Well-Equipped", "Collect 10 styles", 10, IconId.STYLES);

        // Put the Fun in Malfunction?
        addFeat(feats, 3088059, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Nothin' But Chasm",
                "During the Phantom Zone Operation,", 10, IconId.ALERTS);
        // Nothin' But Chasm?
        addFeat(feats, 3088101, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Put the Fun in Malfunction",
                "During the Phantom Zone Operation,", 10, IconId.ALERTS);
        // Kryptonian Cabal?
        addFeat(feats, 3088127, "Episodes", "Phantom Zone & Science Spire", 150, 210, "PZ",
                "During the Phantom Zone Operation,", 10, IconId.ALERTS);
        addFeat(feats, 3088160, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Supply Shortage",
                "During the Phantom Zone Operation defeat the final bosses on the first attempt without allowing any of the 6 Supply Containers from being taken",
                10, IconId.ALERTS);
        // Funstone?
        addFeat(feats, 3088301, "Episodes", "Phantom Zone & Science Spire", 150, 210, "PZ",
                "During the Phantom Zone Operation,", 10, IconId.ALERTS);
        addFeat(feats, 3096155, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Portal Patrol",
                "During the Phantom Zone Operation, prevent all of Zod's forces in the first room of Fort Rozz from escaping through the portal",
                10, IconId.ALERTS);
        // None Shall Pass? or Lights On, Lights Off?
        addFeat(feats, 3135541, "Episodes", "Phantom Zone & Science Spire", 150, 210, "PZe",
                "During the Phantom Zone (Elite) Operation,", 10, IconId.ALERTS);
        addFeat(feats, 3135577, "Episodes", "Phantom Zone & Science Spire", 150, 210, "PZe",
                "During the Phantom Zone (Elite) Operation,", 10, IconId.ALERTS);
        addFeat(feats, 3141827, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Krypton-Spite",
                "During the Science Spire Operation,", 25, IconId.SOLOS);
        addFeat(feats, 3141831, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Energizer",
                "During the Science Spire Operation,", 25, IconId.SOLOS);
        addFeat(feats, 3141835, "Episodes", "Phantom Zone & Science Spire", 150, 210, "Insecurity Devices",
                "During the Science Spire Operation,", 25, IconId.SOLOS);

        addFeat(feats, 3150281, "Seasonal", "St. Patrick's Day", 100, 20, "SPD", "", 10, IconId.ST_PATRICKS);

    }

    private static void addWTTeamUp(Map<Long, Feat> feats, long id, String team) {
        addTeamUp(feats, id, 9, "Watchtower Containment Facility", team);
    }

    private static void addWTTeamUpChecklist(Map<Long, Feat> feats, long id, Alignment alignment) {
        addTeamUpChecklist(feats, id, 9, "Watchtower Containment Facility", alignment);
    }

    private static void addWTChecklist(Map<Long, Feat> feats, long id, Alignment alignment) {
        addChecklist(feats, id, 9, "Watchtower Containment Facility", alignment);
    }

    private static void addOanTeamUp(Map<Long, Feat> feats, long id, String team) {
        addTeamUp(feats, id, 12, "Oan Sciencells", team);
    }

    private static void addOanTeamUpChecklist(Map<Long, Feat> feats, long id, Alignment alignment) {
        addTeamUpChecklist(feats, id, 12, "Oan Sciencells", alignment);
    }

    private static void addOanChecklist(Map<Long, Feat> feats, long id, Alignment alignment) {
        addChecklist(feats, id, 12, "Oan Sciencells", alignment);
    }

    private static void addOanFeat(Map<Long, Feat> feats, long id, String name, String desc, int reward) {
        addOanFeat(feats, id, name, desc, reward, null);
    }

    private static void addOanFeat(Map<Long, Feat> feats, long id, String name, String desc, int reward,
            Alignment alignment) {
        addLegendsPvEFeat(feats, id, "Oan Sciencells", 12, name, desc, reward, IconId.ALERTS, alignment);
    }

    private static void addTeamUp(Map<Long, Feat> feats, long id, int order, String name, String team) {
        addLegendsPvEFeat(feats, id, name, order, "Team-Up: " + team,
                "Complete the Legends: " + name
                        + " PvE Event as one member of this team while the other is also in the group",
                10, IconId.ALERTS);
    }

    private static void addTeamUpChecklist(Map<Long, Feat> feats, long id, int order, String name,
            Alignment alignment) {
        addLegendsPvEFeat(feats, id, name, order, "Team-Up Checklist: " + alignment,
                "Complete all of the following " + alignment + " Team-Ups", 25, IconId.ALERTS);
    }

    private static void addChecklist(Map<Long, Feat> feats, long id, int order, String name, Alignment alignment) {
        addLegendsPvEFeat(feats, id, name, order, "Checklist: " + alignment,
                "Complete the Legends: " + name + " PvE Event as each of the following Legends Characters", 10,
                IconId.ALERTS);
    }

    private static void addLegendsPvEFeat(Map<Long, Feat> feats, long id, String subCat, int order, String name,
            String desc, int reward, IconId iconId) {
        addLegendsPvEFeat(feats, id, subCat, order, name, desc, reward, iconId, null);
    }

    private static void addLegendsPvEFeat(Map<Long, Feat> feats, long id, String subCat, int order, String name,
            String desc, int reward, IconId iconId, Alignment alignment) {
        addFeat(feats, id, "Legends PvE", subCat, 0, order, name, desc, reward, iconId, alignment);
    }

    private static void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2,
            String name, String desc, int reward, IconId iconId) {
        addFeat(feats, id, cat, subCat, order1, order2, name, desc, reward, iconId, null);
    }

    private static void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2,
            String name, String desc, int reward, IconId iconId, Alignment alignment) {
        addFeat(feats, id, cat, subCat, order1, order2, name, desc, reward, iconId, alignment, null);
    }

    private static void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2,
            String name, String desc, int reward, IconId iconId, Alignment alignment, MovementMode movementMode) {
        addFeat(feats, id, cat, subCat, order1, order2, name, desc, reward, iconId, alignment, movementMode, null);
    }

    private static void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2,
            String name, String desc, int reward, IconId iconId, Alignment alignment, MovementMode movementMode,
            Origin origin) {
        addFeat(feats, id, cat, subCat, order1, order2, name, desc, reward, iconId, alignment, movementMode, origin,
                null);
    }

    private static void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2,
            String name, String desc, int reward, IconId iconId, Alignment alignment, MovementMode movementMode,
            Origin origin, List<Role> roles) {
        Feat feat = new Feat();
        feat.setId(id);
        feat.setCategory(cat);
        feat.setSubCategory(subCat);
        feat.setOrder1(order1);
        feat.setOrder2(order2);
        Name newName = new Name();
        newName.setEn(name);
        feat.setName(newName);
        Name newDesc = new Name();
        newDesc.setEn(desc);
        feat.setDescription(newDesc);
        feat.setReward(reward);
        feat.setAlignment(alignment);
        feat.setOrigin(origin);
        feat.setRoles(roles);
        feat.setMovementMode(movementMode);
        feat.setIconId(iconId == null ? 0 : iconId.getId());
        feat.setImagePath("/files/dcuo/images/static/items/" + feat.getIconId() + ".png");
        if (!feats.containsKey(feat.getId())) {
            feats.put(feat.getId(), feat);
        }
    }

    public static void showImage(long charId) {
        String fullImageUrl = imageUrl + charId + imageUrl2 + "male";
        try {
            final BufferedImage image = ImageIO.read(new URL(fullImageUrl));

            if (image != null) {
                JFrame f = new JFrame() {
                    private static final long serialVersionUID = 1L;

                    public void paint(Graphics g) {
                        Insets insets = getInsets();
                        g.drawImage(image, insets.left, insets.top, null);
                    }
                };
                f.setVisible(true);
                Insets insets = f.getInsets();
                f.setSize(image.getWidth() + insets.left + insets.right,
                        image.getHeight() + insets.top + insets.bottom);
            }
        } catch (Exception e) {
            logger.error("Exception: " + e, e);
        }
    }

    public static long saveImage(long charId, Gender gender) {
        return saveImage(charId, gender, true);
    }

    private static long saveImage(long charId, Gender gender, boolean useOldId) {
        String fullImageUrl = imageUrl + charId + imageUrl2 + "male";
        long retval = charId;
        try {
            final BufferedImage image = ImageIO.read(new URL(fullImageUrl));

            if (image != null) {
                File dir = new File(imageDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File out = new File(imageDir + charId + ".png");
                ImageIO.write(image, "png", out);
            } else if (useOldId) {
                long oldId = parser.parseCharIdMapping(new URL(characterIdMapUrl + charId));
                if (oldId > 0) {
                    saveImage(oldId, gender, false);
                    retval = oldId;
                }
            }
        } catch (Exception e) {
            logger.error("Exception: " + e, e);
        }
        return retval;
    }

    public static void saveIcon(Long id, String path, String category, String subCategory) {
        File out = new File(imageDir + category + "/" + subCategory + id + ".png");
        String fullImageUrl = serviceHost + path;
        try {
            if (!out.exists()) {
                final BufferedImage image = ImageIO.read(new URL(fullImageUrl));

                if (image != null) {
                    File dir = new File(imageDir + category);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    ImageIO.write(image, "png", out);
                }
            }
        } catch (Exception e) {
            logger.error("Exception: " + e, e);
        }
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
