package com.aksantos.dcuocensus.poi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Episodes {
    private final Map<Integer, Episode> episodeList = new TreeMap<Integer, Episode>();
    
    private static final Episodes instance = new Episodes();

    private Episodes() {
        List<String> missions = new ArrayList<String>();
        missions.add("Ferris Aircraft Duo");
        missions.add("Coast City Duo");

        missions.add("Oan Sciencells");
        missions.add("Coast City");
        missions.add("Coast City Alert");
        missions.add("Scion of Will");
        missions.add("Scion of Fear");
        missions.add("STAR Labs");
        addEpisode(1, "Fight for the Light", missions);
        
        missions = new ArrayList<String>();
        missions.add("Paradox Reapers");
        missions.add("Massive Speed Force Rupture");
        missions.add("Lightning Strikes Bounty or Wanted mission");
        missions.add("Titans' Bounties");
        missions.add("Rogues' Wanted Posters");

        missions.add("Cosmic Treadmill: Flashback");
        addEpisode(2, "Lightning Strikes", missions);
        
        missions = new ArrayList<String>();
        missions.add("Deconstruction");
        missions.add("Prime Avatars");

        missions.add("Battle for Earth Duos");
        missions.add("Battle for Earth duos");
        missions.add("Riverside Hotel");
        missions.add("Gotham Hospital");
        missions.add("Riverfront Center");

        missions.add("South Gotham Courthouse");
        
        missions.add("Prime Battleground");
        missions.add("Themyscira: The Gates of Tartarus");
        missions.add("Themyscira");
        addEpisode(3, "Battle for Earth", missions);
        
        missions = new ArrayList<String>();
        missions.add("Shady Nightclub");
        missions.add("Police Station");

        missions.add("Assault: HQ");
        missions.add("Nightclub Safehouse");
        missions.add("Police Station Safehouse");
        missions.add("Assault: HQ: Hall of Doom");
        missions.add("Assault: HQ: Watchtower");
        missions.add("Safehouse");
        missions.add("Assault: Safehouse");
        addEpisode(4, "The Last Laugh", missions);

        missions = new ArrayList<String>();
        missions.add("Soul Alchemy");
        missions.add("Wayward Souls");

        missions.add("Unpaid Dues");
        missions.add("Seeds of Rot");
        missions.add("With A Vengeance");
        missions.add("Black Dawn");
        addEpisode(5, "Hand of Fate", missions);

        missions = new ArrayList<String>();
        missions.add("Stryker's Island");
        missions.add("Stryker's Critical Defender Bounty");
        missions.add("Solitary Riot");
        missions.add("Ace Chemicals");
        missions.add("Ace Chemicals Experiment Wanted");
        missions.add("Arkham Island");
        missions.add("Second-Generation Morrowbots Wanted");
        missions.add("Make Them Mad");
        missions.add("Arkham Asylum");
        missions.add("Steelworks");
        missions.add("Breaking Steel");
        missions.add("Oolong Siege Robot Wanted");

        missions.add("Lair PvP Battle");
        addEpisode(6, "Home Turf", missions);
        
        missions = new ArrayList<String>();
        missions.add("Iconic Anomaly: Test Subject #1");
        missions.add("Iconic Anomaly: The Hunt");
        missions.add("Iconic challenge");

        missions.add("Brothers in Arms");
        missions.add("Alternate Metropolis");
        missions.add("Luthor Tower");
        missions.add("Jor-El");

        missions.add("Family Reunion");
        missions.add("Wayne Tower");
        missions.add("Park Row Theater");

        missions.add("Paradox Ruptures");
        missions.add("Paradox Prowler");
        missions.add("Paradox Eradicator");
        missions.add("Speed Force tunnel");

        missions.add("Paradox Wave");
        missions.add("Nexus of Reality");
        addEpisode(7, "Origin Crisis", missions);
        
        missions = new ArrayList<String>();
        missions.add("Gotham Wastelands");
        missions.add("Take Pride");
        
        missions.add("Tunnel of Lust");
        missions.add("Ruined Cathedral");
        missions.add("Knightsdome Arena");
        missions.add("Sons of Trigon Duos");
        
        missions.add("Trigon's Prison");
        addEpisode(8, "Sons of Trigon", missions);
        
        missions = new ArrayList<String>();
        missions.add("Downtown Metropolis Battlezone");
        missions.add("Furious or Fearsome iconic villains");
        missions.add("Hopeful or Unyielding iconic heroes");
        missions.add("Unyielding Hawkman");
        missions.add("Fearsome Black Adam");
        
        missions.add("Strike Team");
        missions.add("Mist Recovery");
        
        missions.add("Assault and Battery");
        addEpisode(9, "War of the Light Part I", missions);

        missions = new ArrayList<String>();
        missions.add("Iconic Vision: Circe's Trial");
        missions.add("Iconic Vision: Aegis of Truth");
        missions.add("Gotham Under Siege");
        missions.add("Flock, Stock, and Barrel");
        
        missions.add("Themyscira: Port of Paradise");
        missions.add("Themyscira: Supply Lines");

        missions.add("Themyscira Divided");
        addEpisode(10, "Amazon Fury Part I", missions);

        missions = new ArrayList<String>();
        missions.add("Intergang Crime Wave");
        missions.add("League Hall: Security Breach");
        
        missions.add("Necropolis: Relics of Urgrund");
        missions.add("Artifacts from the Past");
        missions.add("League Hall: Lockdown");
        missions.add("detaining rifle");
        missions.add("Artifacts of Urgrund Replica Base Items");
        addEpisode(11, "Halls of Power Part I", missions);

        missions = new ArrayList<String>();
        missions.add("Spark of Parallax");
        missions.add("Metropolis Battlezone");
        missions.add("'s Historic District");
        missions.add("WANTED:");
        missions.add("BOUNTY:");
        missions.add("Doctor Light");
        missions.add("Superman");

        missions.add("Spark of Ion");

        missions.add("Zamaron Conversion Chamber");
        missions.add("Avarice Impurity");
        missions.add("Rage Impurity");
        
        missions.add("Love and War");
        missions.add("Cain Street Mall");
        missions.add("Living Planet Base Items");
        addEpisode(12, "War of the Light Part II", missions);

        missions = new ArrayList<String>();
        missions.add("Patrol Catastrophe");

        missions.add("Act of Defiance");
        missions.add("Return to the Nexus");
        
        missions.add("Labyrinth of Lost Souls");
        missions.add("Halls of Hades");
        missions.add("Throne of the Dead");
        missions.add("Throne of Hades");
        missions.add("(Elite)");
        
        missions.add("Base Items");
        addEpisode(13, "Amazon Fury Part II", missions);

        missions = new ArrayList<String>();
        missions.add("New Genesis");
        missions.add("New Genesis Assault");
        missions.add("New Genesis Defense");
        missions.add("WANTED: Apokoliptian Commander");
        missions.add("BOUNTY: New Genesis Protector");

        missions.add("League Hall: Malfunction");
        missions.add("Resource Recovery");

        missions.add("Fatal Exams");
        
        missions.add("New Genesis Now");
        missions.add("Happiness Home");
        missions.add("(Elite)");

        missions.add("Base Items");
        addEpisode(14, "Halls of Power Part II", missions);
    }

    private void addEpisode(int number, String name, List<String> missions) {
        Episode ep = new Episode(number, name, missions);
        episodeList.put((ep.getNumber() - 1) * 10, ep);
    }

    public static Episodes getInstance() {
        return instance;
    }
    
    public Episode getEpisode(int index) {
        return episodeList.get(index);
    }
    
}
