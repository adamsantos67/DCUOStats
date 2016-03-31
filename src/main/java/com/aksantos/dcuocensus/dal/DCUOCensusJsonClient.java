package com.aksantos.dcuocensus.dal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aksantos.dcuocensus.models.AugSocketColor;
import com.aksantos.dcuocensus.models.AugSocketColorList;
import com.aksantos.dcuocensus.models.CharCompletedFeat;
import com.aksantos.dcuocensus.models.CharCompletedFeatList;
import com.aksantos.dcuocensus.models.CharIdMapping;
import com.aksantos.dcuocensus.models.CharIdMappingList;
import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.CharacterList;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.CharactersItemList;
import com.aksantos.dcuocensus.models.Count;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.FeatCategory;
import com.aksantos.dcuocensus.models.FeatCategoryList;
import com.aksantos.dcuocensus.models.FeatList;
import com.aksantos.dcuocensus.models.Item;
import com.aksantos.dcuocensus.models.ItemCategory;
import com.aksantos.dcuocensus.models.ItemCategoryList;
import com.aksantos.dcuocensus.models.ItemList;
import com.aksantos.dcuocensus.models.League;
import com.aksantos.dcuocensus.models.LeagueList;
import com.aksantos.dcuocensus.models.LeagueRoster;
import com.aksantos.dcuocensus.models.LeagueRosterList;
import com.aksantos.dcuocensus.models.Name;
import com.aksantos.dcuocensus.models.Personality;
import com.aksantos.dcuocensus.models.PersonalityList;
import com.aksantos.dcuocensus.models.Reward;
import com.aksantos.dcuocensus.models.RewardList;
import com.aksantos.dcuocensus.models.Type;
import com.aksantos.dcuocensus.models.TypeHolder;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.Gender;
import com.aksantos.dcuocensus.models.enums.IconId;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Power;
import com.aksantos.dcuocensus.models.enums.Role;
import com.aksantos.dcuocensus.models.enums.Weapon;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DCUOCensusJsonClient implements DCUOCensusClient {
    private static final Logger logger = LogManager.getLogger(DCUOCensusJsonClient.class);

    private static final String serviceHost = "http://census.daybreakgames.com";
    private static final String serviceUrl = serviceHost + "/s:BluesStats/get/dcuo:v1/";
    private static final String rewardUrl = serviceUrl + "feat_reward?c:limit=100";
    private static final String featsUrl = serviceUrl + "feat?c:limit=10000";
    private static final String itemsUrl = serviceUrl + "item?c:limit=10000&item_category_id=";
    private static final String itemByIdUrl = serviceUrl + "item?item_id=";
    private static final String featCategoriesUrl = serviceUrl + "feat_category?c:limit=1000";
    private static final String itemCategoriesUrl = serviceUrl + "item_category?c:limit=1000";
    private static final String personalityUrl = serviceUrl + "personality?c:limit=1000";
    private static final String socketUrl = serviceUrl + "aug_socket_color?c:limit=1000";
    private static final String characterUrl = serviceUrl + "character?world_id=2&name=";
    private static final String characterByIdUrl = serviceUrl + "character?character_id=";
    private static final String characterIdMapUrl = serviceUrl + "char_id_mapping?new_character_id=";
    private static final String characterItemsUrl = serviceUrl + "characters_item?c:limit=1000&character_id=";

    private static final String characterQuery = "&c:lang=en";

    private static final String characterFeatsUrl = serviceUrl
            + "characters_completed_feat/?c:limit=10000&character_id=";

    private static final String leagueRosterByCharIdUrl = serviceUrl + "guild_roster?character_id=";
    private static final String leagueRosterByLeagueIdUrl = serviceUrl + "guild_roster?guild_id=";
    private static final String leagueByIdUrl = serviceUrl + "guild?guild_id=";

    private static final String serviceCountUrl = serviceHost + "/s:BluesStats/count/dcuo:v1/";
    private static final String characterFeatCountUrl = serviceCountUrl + "characters_completed_feat?feat_id=";

    private static final String imageUrl = serviceHost + "/files/dcuo/images/character/paperdoll/";
    private static final String imageUrl2 = ".png?fallback=dcuo.paperdoll.";
    private static final String imageDir = "images/";

    private static final String rolePatternStr = "Has([a-zA-Z]*)Role";
    private static final String movementPatternStr = "HasMovementMode mode=([a-zA-Z]*)";
    private static final String factionPatternStr = "Is(Hero|Villain)";
    private static final String originPatternStr = "Is(Meta|Magic|Tech)";

    private static final Pattern rolePattern = Pattern.compile(rolePatternStr);
    private static final Pattern movementPattern = Pattern.compile(movementPatternStr);
    private static final Pattern factionPattern = Pattern.compile(factionPatternStr);
    private static final Pattern originPattern = Pattern.compile(originPatternStr);

    private Map<Long, ItemCategory> itemCategories = null;
    private Map<Long, Personality> personalities = null;
    private Map<Long, FeatCategory> featCategories = null;
    private Map<Long, Reward> rewards = null;
    private Map<Long, League> leagues = null;
    private Map<Long, AugSocketColor> sockets = null;

    private ObjectMapper mapper = new ObjectMapper();

    public Set<Long> getCharacterFeats(Character character) throws DCUOException {
        Set<Long> feats = null;
        try {
            feats = parseCharacterFeats(new URL(characterFeatsUrl + character.getId()), character.getName());
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return feats;
    }

    private Set<Long> parseCharacterFeats(URL jsonUrl, String charName) {
        Set<Long> feats = new TreeSet<Long>();
        if (jsonUrl != null) {
            try {
                CharCompletedFeatList mappings = mapper.readValue(jsonUrl, CharCompletedFeatList.class);
                for (CharCompletedFeat compFeat : mappings.getObjectList()) {
                    feats.add(compFeat.getFeatId());
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException pe) {
                logger.error("IOException while getting feats for " + charName, pe);
            }
        }
        return feats;
    }

    public long getFeatCompletedCount(Feat feat) throws DCUOException {
        long count = 0;
        try {
            count = parseCount(new URL(characterFeatCountUrl + feat.getId()), feat.getNameEn());
        } catch (Exception e) {
            throw new DCUOException("Failed due to exception getting completed count for " + feat.getNameEn(), e);
        }
        return count;
    }

    private long parseCount(URL jsonUrl, String featName) {
        long count = 0;
        if (jsonUrl != null) {
            try {
                Count countVal = mapper.readValue(jsonUrl, Count.class);
                if (countVal != null) {
                    count = countVal.getCount();
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.warn("IOException getting count for " + featName);
                logger.debug("Stack trace: ", e);
            }
        }
        return count;
    }

    public long getOldCharacterId(long newCharacterId) throws DCUOException {
        long oldId = 0;
        try {
            oldId = parseCharIdMapping(new URL(characterIdMapUrl + newCharacterId));
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return oldId;
    }

    private long parseCharIdMapping(URL jsonUrl) {
        long oldId = 0;
        if (jsonUrl != null) {
            try {
                CharIdMappingList mappings = mapper.readValue(jsonUrl, CharIdMappingList.class);
                List<CharIdMapping> mappingList = mappings.getObjectList();
                if (mappingList != null && !mappingList.isEmpty()) {
                    oldId = mappingList.get(0).getOldId();
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return oldId;
    }

    public List<CharactersItem> getCharacterItems(Character character) throws DCUOException {
        List<CharactersItem> retval = null;
        try {
            retval = parseCharacterItems(new URL(characterItemsUrl + character.getId()));
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private List<CharactersItem> parseCharacterItems(URL charUrl) {
        List<CharactersItem> charItems = new ArrayList<CharactersItem>();

        if (charUrl != null) {
            try {
                CharactersItemList charactersItems = mapper.readValue(charUrl, CharactersItemList.class);
                List<CharactersItem> charList = charactersItems.getObjectList();
                charItems.addAll(charList);
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
            Collections.sort(charItems);
        }
        return charItems;
    }

    public Character getCharacter(String characterName) throws DCUOException {
        Character retval = null;
        try {
            String encodedName = URLEncoder.encode(characterName, "UTF-8");
            retval = parseCharacters(new URL(characterUrl + encodedName + characterQuery));
        } catch (UnsupportedEncodingException e) {
            throw new DCUOException(e);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    @Override
    public Character getCharacterById(long id) throws DCUOException {
        Character retval = null;
        try {
            retval = parseCharacters(new URL(characterByIdUrl + id + characterQuery));
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Character parseCharacters(URL charUrl) throws DCUOException {
        Character character = new Character();

        if (charUrl != null) {
            try {
                if (personalities == null) {
                    personalities = getPersonalities();
                }

                CharacterList characters = mapper.readValue(charUrl, CharacterList.class);
                List<Character> charList = characters.getObjectList();
                if (charList.size() > 0) {
                    character = charList.get(0);

                    character.setPersonality(personalities.get(character.getPersonalityId()));
                    character.setMovementMode(MovementMode.getById(character.getMovementModeId()));
                    character.setAlignment(Alignment.getById(character.getAlignmentId()));
                    character.setGender(Gender.getById(character.getGenderId()));
                    character.setPower(Power.getById(character.getPowerTypeId()));
                    character.setWeapon(Weapon.getById(character.getPowerSourceId()));
                    character.setOrigin(Origin.getById(character.getOriginId()));
                    switch (character.getAlignment()) {
                    case Hero:
                        switch (character.getOrigin()) {
                        case Magic:
                            character.setMentor("Wonder Woman");
                            break;

                        case Meta:
                            character.setMentor("Superman");
                            break;

                        case Tech:
                            character.setMentor("Batman");
                            break;
                        }
                        break;

                    case Villain:
                        switch (character.getOrigin()) {
                        case Magic:
                            character.setMentor("Circe");
                            break;

                        case Meta:
                            character.setMentor("Lex Luthor");
                            break;

                        case Tech:
                            character.setMentor("The Joker");
                            break;
                        }
                        break;

                    default:
                        break;
                    }

                    character.setRole(character.getPower().getRole());
                    character.setLeague(getLeague(character));
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return character;
    }

    public Map<Long, Feat> getFeats() throws DCUOException {
        Map<Long, Feat> retval = null;
        try {
            retval = parseFeats(new URL(featsUrl));

            addMissingFeats(retval);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, Feat> parseFeats(URL featUrl) throws DCUOException {
        Map<Long, Feat> feats = new TreeMap<Long, Feat>();

        if (featUrl != null) {
            try {
                FeatList featList = mapper.readValue(featUrl, FeatList.class);

                if (featCategories == null) {
                    featCategories = getFeatCategories();
                }

                if (rewards == null) {
                    rewards = getRewards();
                }

                for (Feat feat : featList.getObjectList()) {
                    FeatCategory category = featCategories.get(feat.getCategoryId());
                    if (category != null) {
                        FeatCategory parentCat = featCategories.get(category.getParentId());
                        if (parentCat != null) {
                            feat.setCategory(parentCat.getName().getEn());
                            feat.setOrder1(parentCat.getOrder());
                            feat.setSubCategory(category.getName().getEn());
                            feat.setOrder2(category.getOrder());
                        } else {
                            feat.setCategory(category.getName().getEn());
                            feat.setOrder1(category.getOrder());
                        }
                    }
                    String predicate = feat.getPredicate();
                    if (predicate != null && predicate.length() > 0) {
                        feat.setAlignments(findPatterns(factionPattern, predicate, Alignment.class));
                        feat.setOrigins(findPatterns(originPattern, predicate, Origin.class));
                        feat.setRoles(findPatterns(rolePattern, predicate, Role.class));
                        feat.setMovementModes(findPatterns(movementPattern, predicate, MovementMode.class));
                    }
                    if ("Quick to Defend".equals(feat.getNameEn())) {
                        if (feat.getDescriptionEn().contains("Mogo")) {
                            feat.setAlignment(Alignment.Hero);
                        } else {
                            feat.setAlignment(Alignment.Villain);
                        }
                    } else if ("Four Corners".equals(feat.getNameEn())) {
                        if (feat.getDescriptionEn().contains("Ranx")) {
                            feat.setAlignment(Alignment.Hero);
                        } else {
                            feat.setAlignment(Alignment.Villain);
                        }
                    }
                    Long rewardId = feat.getRewardId();
                    Reward reward = rewards.get(rewardId);
                    if (reward != null) {
                        feat.setReward(reward.getXp());
                    }
                    feats.put(feat.getId(), feat);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return feats;
    }

    public Map<Long, Item> getItems() throws DCUOException {
        Map<Long, Item> retval = null;
        try {
            if (itemCategories == null) {
                getItemCategories();
            }

            for (ItemCategory category : itemCategories.values()) {
                Map<Long, Item> itemsByCat = parseItems(new URL(itemsUrl + category.getId()));
                if (retval == null) {
                    retval = itemsByCat;
                } else {
                    retval.putAll(itemsByCat);
                }
            }
            addMissingItems(retval);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    public Item getItem(long itemId) throws DCUOException {
        Item retval = null;
        try {
            Map<Long, Item> items = parseItems(new URL(itemByIdUrl + itemId));
            if (items != null) {
                retval = items.get(itemId);
            }
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, Item> parseItems(URL itemUrl) throws DCUOException {
        Map<Long, Item> items = new TreeMap<Long, Item>();

        if (itemUrl != null) {
            try {
                if (itemCategories == null) {
                    itemCategories = getItemCategories();
                }

                if (sockets == null) {
                    sockets = getSockets();
                }

                ItemList itemList = mapper.readValue(itemUrl, ItemList.class);

                for (Item item : itemList.getItem_list()) {
                    ItemCategory category = itemCategories.get(item.getCategoryId());
                    if (category != null) {
                        ItemCategory parentCat = itemCategories.get(category.getParentId());
                        if (parentCat != null) {
                            item.setCategory(parentCat);
                            item.setSubCategory(category);
                        } else {
                            item.setCategory(category);
                        }
                    }

                    AugSocketColor socket = sockets.get(item.getSocketColorId1());
                    if (socket != null) {
                        item.setSocketColor1(socket.getName());
                    }
                    socket = sockets.get(item.getSocketColorId2());
                    if (socket != null) {
                        item.setSocketColor2(socket.getName());
                    }

                    long alignmentId = item.getAlignmentId();
                    item.setAlignment(Alignment.getById(alignmentId));

                    items.put(item.getId(), item);

                    saveIcon(item);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return items;
    }

    public League getLeague(Character character) throws DCUOException {
        League retval = null;
        try {
            Map<Long, LeagueRoster> leagueRosters = parseTypes(new URL(leagueRosterByCharIdUrl + character.getId()),
                    LeagueRosterList.class);
            if (leagueRosters != null && !leagueRosters.isEmpty()) {
                long leagueId = 0;
                for (LeagueRoster roster : leagueRosters.values()) {
                    leagueId = roster.getId();
                    break;
                }
                if (leagues == null) {
                    leagues = new TreeMap<Long, League>();
                } else {
                    retval = leagues.get(leagueId);
                }
                if (retval == null) {
                    retval = getLeagueById(leagueId);
                    leagues.put(retval.getId(), retval);
                }
            }
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    public League getLeagueById(long id) throws DCUOException {
        League retval = null;
        Map<Long, League> leagueList = null;
        try {
            leagueList = parseTypes(new URL(leagueByIdUrl + id), LeagueList.class);
            if (leagueList != null && !leagueList.isEmpty()) {
                for (League league : leagueList.values()) {
                    retval = league;
                    break;
                }
            }
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    @Override
    public List<LeagueRoster> getLeagueRoster(long id) throws DCUOException {
        List<LeagueRoster> retval = new ArrayList<LeagueRoster>();
        try {
            Map<Long, LeagueRoster> leagueRosters = parseTypes(new URL(leagueRosterByLeagueIdUrl + id),
                    LeagueRosterList.class);
            if (leagueRosters != null && !leagueRosters.isEmpty()) {
                retval.addAll(leagueRosters.values());
            }
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, Reward> getRewards() throws DCUOException {
        Map<Long, Reward> retval = null;
        try {
            retval = parseTypes(new URL(rewardUrl), RewardList.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, FeatCategory> getFeatCategories() throws DCUOException {
        Map<Long, FeatCategory> retval = null;
        try {
            retval = parseTypes(new URL(featCategoriesUrl), FeatCategoryList.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    public Map<Long, ItemCategory> getItemCategories() throws DCUOException {
        Map<Long, ItemCategory> retval = null;
        if (itemCategories != null) {
            retval = itemCategories;
        } else {
            try {
                retval = parseTypes(new URL(itemCategoriesUrl), ItemCategoryList.class);
            } catch (MalformedURLException e) {
                throw new DCUOException(e);
            }
            itemCategories = retval;
        }
        return retval;
    }

    private Map<Long, Personality> getPersonalities() throws DCUOException {
        Map<Long, Personality> retval = null;
        try {
            retval = parseTypes(new URL(personalityUrl), PersonalityList.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, AugSocketColor> getSockets() throws DCUOException {
        Map<Long, AugSocketColor> retval = null;
        try {
            retval = parseTypes(new URL(socketUrl), AugSocketColorList.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private <T extends Type, H extends TypeHolder<T>> Map<Long, T> parseTypes(URL jsonUrl, Class<H> holderClass) {
        Map<Long, T> typeMap = new TreeMap<Long, T>();

        if (jsonUrl != null) {
            try {
                H holder = mapper.readValue(jsonUrl, holderClass);
                for (T type : holder.getObjectList()) {
                    typeMap.put(type.getId(), type);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return typeMap;
    }

    private <E extends Enum<E>> List<E> findPatterns(Pattern pattern, String predicateStr, Class<E> type) {
        List<E> retvals = new ArrayList<E>();
        Matcher matcher = pattern.matcher(predicateStr);
        while (matcher.find()) {
            retvals.add(E.valueOf(type, matcher.group(1)));
        }
        return retvals;
    }

    public void saveImage(Character character) {
        character.setImageId(saveImage(character.getId(), character.getGender(), true));
    }

    private long saveImage(long charId, Gender gender, boolean useOldId) {
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
                long oldId = getOldCharacterId(charId);
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

    public void saveIcon(Item item) {
        ItemCategory category = item.getCategory();
        ItemCategory subCategory = item.getSubCategory();
        saveIcon(item.getIconId(), item.getImagePath(), category == null ? "" : category.getCategoryName(),
                subCategory == null ? "" : subCategory.getCategoryName());
    }

    public void saveIcon(Feat feat) {
        saveIcon(feat.getIconId(), feat.getImagePath(), "Feat", "Icon");
    }

    private void saveIcon(Long id, String path, String category, String subCategory) {
        if (id > 0) {
            File out = new File(imageDir + category + "/" + subCategory + id + ".png");
            String fullImageUrl = serviceHost + path;
            try {
                if (!out.exists()) {
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(new URL(fullImageUrl));
                    } catch (IIOException e) {
                        image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
                        logger.warn("Exception: " + e.getCause());
                    }

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

        addFeat(feats, 3150281, "Seasonal", "St. Patrick's Day", 100, 20, "Luck o' the 5th Dimension",
                "Collect any 8 Base Items from the 2016 St. Patrick's Day event", 10, IconId.ST_PATRICKS);
        addFeat(feats, 3302238, "Seasonal", "St. Patrick's Day", 100, 20, "Shamrock & Roll", "Use 20 Clover Bombs", 10,
                IconId.ST_PATRICKS);

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

    private void addMissingItems(Map<Long, Item> items) {
        long id = 3099253;
        if (!items.containsKey(id)) {
            Item item = new Item();
            item.setId(id);
            item.setNameEn("Utility Belt of Omnipotence");
            item.setItemLevel(136);
            item.setRequiredCR(30);
            item.setQuality(6);
            item.setIconId(103192);
            item.setCategoryId(2026161);
            item.setCategory(itemCategories.get(2026163L));
            item.setSubCategory(itemCategories.get(2026161L));
            item.setSaleValue(2294);
            item.setMitigation(505);
            item.setHealth(1232);
            item.setPower(474);
            item.setFinisherAttack(188);
            item.setBasicAttack(480);
            item.setHeal(501);
            item.setPowerHeal(324);
            item.setDominance(91);
            item.setNoTrade(true);
            items.put(item.getId(), item);
        }
    }

}
