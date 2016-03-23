package com.aksantos.dcuocensus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.Gender;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.aksantos.dcuocensus.models.enums.Origin;
import com.aksantos.dcuocensus.models.enums.Power;
import com.aksantos.dcuocensus.models.enums.Weapon;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CensusParser {
    private static final Logger logger = LogManager.getLogger(CensusParser.class);

    private static final String rolePatternStr = "Has([a-zA-Z]*)Role";
    private static final String movementPatternStr = "HasMovementMode mode=([a-zA-Z]*)";
    private static final String factionPatternStr = "Is(Hero|Villain)";
    private static final String originPatternStr = "Is(Meta|Magic|Tech)";

    private static final Pattern rolePattern = Pattern.compile(rolePatternStr);
    private static final Pattern movementPattern = Pattern.compile(movementPatternStr);
    private static final Pattern factionPattern = Pattern.compile(factionPatternStr);
    private static final Pattern originPattern = Pattern.compile(originPatternStr);

    private ObjectMapper mapper = new ObjectMapper();

    public Set<Long> parseCharacterFeats(URL jsonUrl, String charName) {
        Set<Long> feats = new TreeSet<Long>();
        if (jsonUrl != null) {
            try {
                CharCompletedFeats mappings = mapper.readValue(jsonUrl, CharCompletedFeats.class);
                for (CharCompletedFeat compFeat : mappings.getFeatList()) {
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

    public long parseCount(URL jsonUrl, String featName) {
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

    public long parseCharIdMapping(URL jsonUrl) {
        long oldId = 0;
        if (jsonUrl != null) {
            try {
                CharIdMappings mappings = mapper.readValue(jsonUrl, CharIdMappings.class);
                List<CharIdMapping> mappingList = mappings.getMappingList();
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

    public Map<Long, Reward> parseRewards(URL rewardsUrl) {
        Map<Long, Reward> rewards = new TreeMap<Long, Reward>();

        if (rewardsUrl != null) {
            try {
                Rewards rewardList = mapper.readValue(rewardsUrl, Rewards.class);

                for (Reward reward : rewardList.getFeatRewardList()) {
                    rewards.put(reward.getId(), reward);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return rewards;
    }

    public List<CharactersItem> parseCharacterItems(URL charUrl) {
        List<CharactersItem> charItems = new ArrayList<CharactersItem>();

        if (charUrl != null) {
            try {
                CharactersItems charactersItems = mapper.readValue(charUrl, CharactersItems.class);
                List<CharactersItem> charList = charactersItems.getCharactersItemList();
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

    public Character parseCharacters(URL charUrl, Map<Long, Personality> personalities,
            Map<Long, Alignment> alignments) {
        Character character = new Character();

        if (charUrl != null) {
            try {
                Characters characters = mapper.readValue(charUrl, Characters.class);
                List<Character> charList = characters.getCharacterList();
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

    public Map<Long, Feat> parseFeats(URL featUrl, Map<Long, FeatCategory> categories, Map<Long, Reward> rewards,
            Map<Long, Alignment> alignments) {
        Map<Long, Feat> feats = new TreeMap<Long, Feat>();

        if (featUrl != null) {
            try {
                Feats featList = mapper.readValue(featUrl, Feats.class);

                for (Feat feat : featList.getFeatList()) {
                    FeatCategory category = categories.get(feat.getCategoryId());
                    if (category != null) {
                        FeatCategory parentCat = categories.get(category.getParentId());
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
                        String alignStr = findPatterns(factionPattern, predicate);
                        if (alignStr != null && alignStr.length() > 0) {
                            feat.setAlignment(Alignment.valueOf(alignStr));
                        }
                        feat.setOriginStr(findPatterns(originPattern, predicate));
                        feat.setRole(findPatterns(rolePattern, predicate));
                        feat.setMovementModeStr(findPatterns(movementPattern, predicate));
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

    public Map<Long, Item> parseItems(URL itemUrl, Map<Long, ItemCategory> categories) {
        Map<Long, Item> items = new TreeMap<Long, Item>();

        if (itemUrl != null) {
            try {
                Items itemList = mapper.readValue(itemUrl, Items.class);

                for (Item item : itemList.getItem_list()) {
                    ItemCategory category = categories.get(item.getCategoryId());
                    if (category != null) {
                        ItemCategory parentCat = categories.get(category.getParentId());
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

    private String findPatterns(Pattern pattern, String predicateStr) {
        Matcher matcher = pattern.matcher(predicateStr);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(matcher.group(1));
        }
        return sb.toString();
    }

    public Map<Long, FeatCategory> parseFeatCategories(URL categoryUrl) {
        Map<Long, FeatCategory> categories = new TreeMap<Long, FeatCategory>();

        if (categoryUrl != null) {
            try {
                FeatCategories categoryList = mapper.readValue(categoryUrl, FeatCategories.class);
                for (FeatCategory featCategory : categoryList.getFeatCategoryList()) {
                    categories.put(featCategory.getId(), featCategory);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return categories;
    }

    public Map<Long, ItemCategory> parseItemCategories(URL categoryUrl) {
        Map<Long, ItemCategory> categories = new TreeMap<Long, ItemCategory>();

        if (categoryUrl != null) {
            try {
                ItemCategories categoryList = mapper.readValue(categoryUrl, ItemCategories.class);
                for (ItemCategory itemCategory : categoryList.getItemCategoryList()) {
                    categories.put(itemCategory.getId(), itemCategory);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return categories;
    }

    public Map<Long, Personality> parsePersonalities(URL categoryUrl) {
        Map<Long, Personality> personalityMap = new TreeMap<Long, Personality>();

        if (categoryUrl != null) {
            try {
                Personalities personalities = mapper.readValue(categoryUrl, Personalities.class);
                for (Personality personality : personalities.getPersonalityList()) {
                    personalityMap.put(personality.getId(), personality);
                }
            } catch (JsonParseException e) {
                logger.error("Exception: " + e, e);
            } catch (JsonMappingException e) {
                logger.error("Exception: " + e, e);
            } catch (IOException e) {
                logger.error("Exception: " + e, e);
            }
        }
        return personalityMap;
    }
}
