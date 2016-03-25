package com.aksantos.dcuocensus;

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

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aksantos.dcuocensus.models.CharCompletedFeat;
import com.aksantos.dcuocensus.models.CharCompletedFeats;
import com.aksantos.dcuocensus.models.CharIdMapping;
import com.aksantos.dcuocensus.models.CharIdMappings;
import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.Characters;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.CharactersItems;
import com.aksantos.dcuocensus.models.Count;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.FeatCategories;
import com.aksantos.dcuocensus.models.FeatCategory;
import com.aksantos.dcuocensus.models.Feats;
import com.aksantos.dcuocensus.models.Item;
import com.aksantos.dcuocensus.models.ItemCategories;
import com.aksantos.dcuocensus.models.ItemCategory;
import com.aksantos.dcuocensus.models.Items;
import com.aksantos.dcuocensus.models.Personalities;
import com.aksantos.dcuocensus.models.Personality;
import com.aksantos.dcuocensus.models.Reward;
import com.aksantos.dcuocensus.models.Rewards;
import com.aksantos.dcuocensus.models.Type;
import com.aksantos.dcuocensus.models.TypeHolder;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.Gender;
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
                CharCompletedFeats mappings = mapper.readValue(jsonUrl, CharCompletedFeats.class);
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
                CharIdMappings mappings = mapper.readValue(jsonUrl, CharIdMappings.class);
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
                CharactersItems charactersItems = mapper.readValue(charUrl, CharactersItems.class);
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

    private Character parseCharacters(URL charUrl) throws DCUOException {
        Character character = new Character();

        if (charUrl != null) {
            try {
                if (personalities == null) {
                    personalities = getPersonalities();
                }

                Characters characters = mapper.readValue(charUrl, Characters.class);
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
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, Feat> parseFeats(URL featUrl) throws DCUOException {
        Map<Long, Feat> feats = new TreeMap<Long, Feat>();

        if (featUrl != null) {
            try {
                Feats featList = mapper.readValue(featUrl, Feats.class);

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
            retval = parseItems(new URL(itemsUrl));
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

                Items itemList = mapper.readValue(itemUrl, Items.class);

                for (Item item : itemList.getItem_list()) {
                    ItemCategory category = itemCategories.get(item.getCategoryId());
                    if (category != null) {
                        ItemCategory parentCat = itemCategories.get(category.getParentId());
                        if (parentCat != null) {
                            item.setCategory(parentCat.getName().getEn());
                            item.setSubCategory(category.getName().getEn());
                        } else {
                            item.setCategory(category.getName().getEn());
                        }
                    }
                    long alignmentId = item.getAlignmentId();
                    item.setAlignment(Alignment.getById(alignmentId));

                    items.put(item.getId(), item);
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

    private Map<Long, Reward> getRewards() throws DCUOException {
        Map<Long, Reward> retval = null;
        try {
            retval = parseTypes(new URL(rewardUrl), Rewards.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, FeatCategory> getFeatCategories() throws DCUOException {
        Map<Long, FeatCategory> retval = null;
        try {
            retval = parseTypes(new URL(featCategoriesUrl), FeatCategories.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, ItemCategory> getItemCategories() throws DCUOException {
        Map<Long, ItemCategory> retval = null;
        try {
            retval = parseTypes(new URL(itemCategoriesUrl), ItemCategories.class);
        } catch (MalformedURLException e) {
            throw new DCUOException(e);
        }
        return retval;
    }

    private Map<Long, Personality> getPersonalities() throws DCUOException {
        Map<Long, Personality> retval = null;
        try {
            retval = parseTypes(new URL(personalityUrl), Personalities.class);
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
        saveIcon(item.getIconId(), item.getImagePath(), item.getCategory(), item.getSubCategory());
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
    }

}
