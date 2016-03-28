package com.aksantos.dcuocensus.models;

import java.util.Comparator;

public class CharacterComparator implements Comparator<Character> {

    public int compare(Character char1, Character char2) {
        int ret = char2.getLevel() - char1.getLevel();
        if (ret == 0) {
            ret = char2.getCombatRating() - char1.getCombatRating();
            if (ret == 0) {
                ret = char2.getSkillPoints() - char1.getSkillPoints();
                if (ret == 0) {
                    ret = char1.getName().compareTo(char2.getName());
                }
            }
        }
        return ret;
    }

}
