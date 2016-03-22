package com.aksantos.dcuocensus;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import com.aksantos.dcuocensus.models.Feat;
import com.aksantos.dcuocensus.models.Feats;
import com.aksantos.dcuocensus.models.Name;
import com.aksantos.dcuocensus.models.enums.Alignment;
import com.aksantos.dcuocensus.models.enums.IconId;
import com.aksantos.dcuocensus.models.enums.MovementMode;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonExample {

    public static void main(String[] args) {
        JacksonExample ex = new JacksonExample();
        ex.run();
        
        ex.run2();
    }

    private void run() {
        ObjectMapper mapper = new ObjectMapper();

        Map<Long, Feat> featMap = new TreeMap<Long, Feat>();
        
        addFeat(featMap, 715084, "General", "", 10, 0, "Agile Ace", "Achieve level 30 with an Agile movement mode", 25,
                IconId.GENERAL, null, MovementMode.Acrobat);
        
        Feats feats = new Feats();
        feats.getFeatList().addAll(featMap.values());
        
        /*
        addFeat(feats, 715085, "General", "", 10, 0, "Aerial Antics", "Achieve level 30 with an Aerial movement mode",
                25, IconId.GENERAL, null, MovementMode.Flight);
        addFeat(feats, 715086, "General", "", 10, 0, "Swiftly Supreme", "Achieve level 30 with a Swift movement mode",
                25, IconId.GENERAL, null, MovementMode.Speed);
        addFeat(feats, 959776, "General", "", 10, 0, "Master Escape Artist", "Use the Breakout ability 100 times", 10,
                IconId.GENERAL, null, null);
*/
        try {
            // Convert object to JSON string and save into a file directly
            mapper.writeValue(new File("feat.json"), feats);

            // Convert object to JSON string
            String jsonInString = mapper.writeValueAsString(feats);
            System.out.println(jsonInString);

            // Convert object to JSON string and pretty print
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(feats);
            System.out.println(jsonInString);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run2() {
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Convert JSON string from file to Object
            Feats staff = mapper.readValue(new File("feat.json"), Feats.class);
            System.out.println(staff);

            Feats feats = mapper.readValue(new URL("http://census.daybreakgames.com/get/dcuo:v1/feat?feat_id=966905"), Feats.class);
            System.out.println(feats);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addFeat(Map<Long, Feat> feats, long id, String cat, String subCat, int order1, int order2, String name,
            String desc, int reward, IconId iconId, Alignment alignment, MovementMode movementMode ) {
        
//    }, Origin origin, String role) {
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
//        feat.setOrigin(origin);
//        feat.setRole(role);
        feat.setMovementMode(movementMode);
        feat.setIconId(iconId == null ? 0 : iconId.getId());
        feat.setImagePath("/files/dcuo/images/static/items/" + feat.getIconId() + ".png");
        if (!feats.containsKey(feat.getId())) {
            feats.put(feat.getId(), feat);
        }
    }

}
