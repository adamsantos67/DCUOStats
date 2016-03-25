package com.aksantos.dcuocensus;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aksantos.dcuocensus.models.Character;
import com.aksantos.dcuocensus.models.CharactersItem;
import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.Item;

public interface DCUOCensusClient {
    Character getCharacter(String characterName) throws DCUOException;

    Set<Long> getCharacterFeats(Character character) throws DCUOException;

    List<CharactersItem> getCharacterItems(Character character) throws DCUOException;

    long getOldCharacterId(long newCharacterId) throws DCUOException;

    Map<Long, Feat> getFeats() throws DCUOException;

    long getFeatCompletedCount(Feat feat) throws DCUOException;

    Map<Long, Item> getItems() throws DCUOException;

    Item getItem(long itemId) throws DCUOException;

    void saveImage(Character character);

    void saveIcon(Item item);

    void saveIcon(Feat feat);
}
