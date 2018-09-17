package com.naxa.conservationtrackingapp.model;

/**
 * Created by user1 on 9/23/2015.
 */
public class Constants {

    public static String  hwcEndomentGPSPointKey = "" , hwcEndomentGPSTrakingKey = "" ;

    public static String[] LANDSCAPE = { "TAL PABZ", "TAL CBRP", "SHL", "CHAL", "NML", "Others"};

    public static String[] statusOfFench = { "Good" , "Broken" };
    public static String[] TREES_PLANTED = { "NTFP","Fodder","Fruits","Others" };
    public static String[] SEASON_PLANTED = { "Summer", "Winter", "Monsoon"};

    public static String[] FOREST_TYPE = {"Natural","Plantation" , "Mixed"};
    public static String[] GRAZING_PRESSURE = {"High" , "Medium"  , "Low"};
    public static String[] NATURAL_REGENERATION = {"High" , "Medium"  , "Low"};
    public static String[] FOREST_CONDITION = {"Poor","Degraded","Average","Good"};
    
    public static String[] FOREST_ENCROACHMENT = {"Yes", "No"};
    public static String[] MOBILIZATION = {"Yes","No"};
    public static String[] CAUSES_FOREST_FIRE = {"Human Induced", "Natural", "Others"};
    public static String[] ITEMS_CONFISCATED = {"Guns","Snares","Khabar nets","Leg hole traps","Bullets","Dead animal or its body parts","Others"};
    public static String[] METHOD_USED = {"Gun Shot","Snares","Khabar nets","Leg hole traps","Poisoning", "Others"};
    public static String[] SPECIES_POACHED_ACTION = {"Poached","Attempted"};
    public static String[] ACTIVITY = {"Watch Tower","APO Post","Bridge (maintenance/construction)","Park/Office Building","Communication Tower", "Others"};
    public static String[] SUPPORT_ACTIVITY = {"Solar Panel", "Drinking Water Supply", "Real Time SMART Patrol", "Sweeping/Company Operation", "Camping Operation", "Mobilizing Informant", "Patrol Equipment", "Camping Gear", "Sniffer Dog", "Arial Survelliance", "CCTV and Smart Eye", "Field Gear", "Others"};


    public static String[] NURSERY_SEEDING_RAISED = {"Amala", "Bans", "Bet", "Bel", "Amriso", "Harro", "Kurilo", "Nim", "Raktachandan", "Rittha", "Sarpagandha", "Tejpat", "Bakaino", "Badhahar", "Kavro", "Kimbu", "Koiralo", "Nimaro", "Tanki", "Asna", "Ashok", "Bijaysal", "Chiuri", "Dhupi","Haldu", "Jamun", "Khayer", "Kusum", "Mahuwa", "Nimaro", "Rajbrikssha", "Satisal", "Siris", "Sissoo", "Simal", "Sindhure", "Bayer", "Lapsi", "Katahar", "Mewa", "others"};
    public static String[] NURSERY_GRASS_SPECIES = {"Babiyo","Napier","Others"};
    public static String[] FOREST_ILM = {"Cement fund support", "Artificial insemination", "Productive cattle breed", "Others"};


    public static String[] FOREST_PROTECTION_FENCH_TRENCH = {"Forest Road", "Fireline" , "Watch Tower", "Guard Post", "Others"};
    public static String[] FOREST_PROTECTION_STATUS = {"Good","Broken"};
    public static String[] FOREST_PROTECTION_RECOM = {"Yes","No"};
    public static String[] FOREST_PROTECTION_IMPLE = {"Yes","No"};
    public static String[] FOREST_PROTECTION_REASON = {"Success","Failure"};

    public static String[] ECONOMIC_WELL_BEING = {"A","B","C","D"};
    public static String[] HUMAN_CASUALTY = {"Death","Injury"};

    public static String[] WELL_BEING_RANK = {"A","B","C","D"};

    public static String[] COMPENSATION = {"Yes","No"};

    public static String[] DAMAGE_STATUS = {"Partial","Complete"};


    public static String[] NAME_OF_ACTIVITY = {"Electric","Solar","Bio fencing","Machan","Trench","Grain storage","Improved corral","Watch tower"};

    public static String[] STATUS_OF_FENCE = {"Good","Damaged"};

    public static String[] ACTIVITY_TYPES = {"Cement fund support","Artificial insemination","Productive cattle breed"};

    public static String[] WMM_WETLANDM_WATERHOLE = {"Construction", "Restoration"};
    public static String[] WMM_WETLANDM_RESTORATION = {"_________", "Spring Protection", "Invasive Species Removal", "Soil Debris Removal", "Others"};
    public static String[] WMM_WETLANDM_STATUS_WILDLIFE_USE = {"Frequent", "Rare", "Not used"};
    public static String[] WMM_WETLANDM_STATUS_WETLAND = {"Good", "Moderate", "Poor"};

    public static String[] WMM_GM_MANAGEMENT_PRAC = {"Grasses Cut", "Trees Uprooted", "Old Grass Burnt", "New Grass Seeding", "Others"};
    public static String[] WMM_GM_ADDITIONAL_INTERVENTIONS = {"Solar Recharger", "Water Sprinkler", "Others"};
    public static String[] WMM_GM_STATUS_WILDLIFE_USE = {"Frequent", "Rare", "Not used"};
    public static String[] WMM_GM_STATUS_GRASSLAND = {"Good", "Degraded"};

    public static String[] WMM_ISM_INVASIVE_SPP_NAME = {"Mikenia", "Lantana camara", "Cromollena", "Others"};
    public static String[] WMM_ISM_MANAGEMENT_PRACTICES = {"Uprooted", "Burnt", "Mix of Uprooting and Burning", "Others"};
    public static String[] WMM_ISM_STATUS_REGROWTH = {"Yes", "No"};
    public static String[] WMM_ISM_STATUS_WILDLIFE_USE = {"Frequent", "Rare", "Not used"};

    public static String[] WMM_WS_HABITAT_TYPE = {"Sal Forest", "Rivarine Forest", "Mixed Forest", "Short Grassland", "Tall Grassland"};
    public static String[] WMM_WS_ACTIVITY = {"Grazing", "Walking", "Wallowing", "Sleeping", "Resting", "Other with specify"};
    public static String[] WMM_WS_SEX = {"Male", "Female"};

    public static String[] WMM_WILDLIFEM_NAME = {"Tiger", "Rhino", "Chital"};
    public static String[] WMM_WILDLIFEM_REASONS = {"Conflict animal", "Injured animal", "Old animal in human settlement", "Others"};
    public static String[] WMM_WILDLIFEM_SEX = {"Male", "Female", "Unknown"};
    public static String[] WMM_WILDLIFEM_AGE = {"Adult", "Sub-Adult", "Calf/Cub"};
    public static String[] WMM_WILDLIFEM_CAUSES = {"Natural Death", "Poaching", "Retaliatory Killing", "Add Poisoning", "Road Accident", "Unknown"};
    public static String[] WMM_WILDLIFEM_SOURCE_INFO = {"Direct Observation", "Secondary Information", "Park Report", "Community Forest Record", "Others"};

    public static String[] WMM_WERR_HEALTH_STATUS = {"Poor", "Healthy", "Injured"};
    public static String[] WMM_WERR_ACTION_TAKEN = {"Transferred to Zoo", "Transferred to Park enclosures", "Others"};

    public static String[] CC_BD_WELL_BEING_STATUS = {"A", "B", "C", "D"};
    public static String[] CC_BD_CAPACITY_OF_BIOGAS = {"4 m3", "6 m3", "8 m3", "10 m3"};
    public static String[] CC_BD_TOILET_ATTACHED = {"Yes", "No"};

    public static String[] CC_ICS_WELL_BEING_STATUS = {"A", "B", "C", "D"};

    public static String[] CS_IS_OFFICE_EQUIPMENT = {"Computer", "Stationary", "Books", "Furniture", "Others"};

    public static String[] CS_IGA_TYPE = {"Agro", "Forest", "Off farm", "Skill based", "Tourism", "Veterinary services NTFP based"};
    public static String[] CS_IGA_TARGET_GROUP = {"HWC affected", "NRM dependant", "Others"};
    public static String[] CS_IGA_WELL_BEING_STATUS = {"A", "B", "C", "D"};

    public static String[] CS_SBT_TARGET_GROUP = {"NRM dependant", "HWC affected", "Others"};

    public static String[] CS_TCMF_TARGET_GROUP = {"NRM based", "HWC affected", "Others"};

    public static String[] CS_RF_TARGET_GROUP = {"NRM based", "HWC affected", "Others"};

    public static String[] CS_CB_ACTIVITY_TYPE = {"Training", "Orientation", "Workshop", "Meeting", "Study Tour", "Others"};
    public static String[] CS_CB_AFFILIATION = {"Government", "Civil Society", "organization", "others"};


    public static String[] CE_ECD_SCHOOL_TYPE = {"Government", "Private", "Public"};
    public static String[] CE_ECD_SCHOOL_LEVEL = {"Primary", "Secondary", "Higher Secondary"};

    public static String[] CE_ECS_SCHOOL_TYPE = {"Government", "Private", "Public"};
    public static String[] CE_ECS_SUPPORT_CATEGORY = {"Institutional building support", "Capacity building", "Awareness"};
    public static String[] CE_ECS_IBS_ = {"Library", "Furniture", "Books/Stationary"};
    public static String[] CE_ECS_CB = {"Various competition", "exposure visits", "study tour"};

    public static String[] CE_CEA_THEME_TYPE = {"Endangered spp", "Anti-poaching", "Forest conservation",  "Climate Change", "Water"};
    public static String[] CE_CEA_SUB_ACTIVITIES = {"Debate", "Essay writing", "Sports", "Poetry", "Cleaning campaign", "Plantation", "Birding", "Forest inventory", "Lok dohori", "Drama", "Others"};

    public static String[] WMT_GMM_BIOLOGICAL_HABITAT_TYPE = {"Sal com.naxa.conservationtracking.forest", "Mixed Forest", "Riverine Forest", "Tall Grassland", "Short Grassland", "Wetland", "Streambed"};
    public static String[] WMT_GMM_BIOLOGICAL_HABITAT_SUBSTRATUM = {"Sandy bank", "Grassy bank", "Muddy soil", "Gravel", "Pebbles", "Rocky area"};
    public static String[] WMT_GMM_BIOLOGICAL_ACTIVITY = {"Basking", "Fishing", "Swimming"};
    public static String[] WMT_GMM_BIOLOGICAL_SPECIES_NAME = {"Gharial", "Mugger"};

    public static String[] WMT_GMM_ANTHROPOGENIC_HABITAT_TYPE = {"Sal com.naxa.conservationtracking.forest", "Mixed Forest", "Riverine Forest", "Tall Grassland", "Short Grassland", "Wetland", "Streambed"};
    public static String[] WMT_GMM_ANTHROPOGENIC_PRESENCE_OF_GHARIAL = {"Yes", "No"};
    public static String[] WMT_GMM_ANTHROPOGENIC_RIVER_POLLUTION = {"Waste", "Effluents", "Domestic Sewage"};
    public static String[] WMT_GMM_ANTHROPOGENIC_PRESENCE_OF_SAND = {"Yes", "No"};

    public static String[] WMT_SCAT_COLLECTION_SEGMENTATION = {"Yes", "No"};
    public static String[] WMT_SCAT_COLLECTION_AGE = {"Old", "Fresh", "Very Fresh"};
    public static String[] WMT_SCAT_COLLECTION_SITE_TYPE = {"Rigid", "Cliff", "Grassland", "Streamline"};
    public static String[] WMT_SCAT_COLLECTION_ASSOCIATED_SIGN = {"Scrape", "Pugmark", "Kill Site", "Scent Spray", "Claw Rake"};

    public static String[] WMT_SNOW_LEOPARD_USE_OF_GRAZING_LAND = {"Year Round", "Seasonal", "Not Used"};
    public static String[] WMT_SNOW_LEOPARD_HABITAT_TYPE = {"Alpine Grassland", "Shurbland", "Barren Land", "Sub Alpine Forest"};
    public static String[] WMT_SNOW_LEOPARD_ASPECT = {"South", "Southwest", "Southeast","North", "Northwest", "Northeast"};
    public static String[] WMT_SNOW_LEOPARD_SLOPE = {"0-30", "31-60", "61-90"};


    public static String[] HWC_YES_NO = {"Yes", "No"};
    public static String[] HWC_ELEPHANT_ACTIVITY_POTENTIAL_ATTRACTANTS = {"Standing crop", "Fruit plantations", "Stored grains", "Stored alcohol", "Unknown", "Others"};
    public static String[] HWC_ELEPHANT_ACTIVITY_REACTION_DURING_ENTRY = {"Ran away from village", "Continued entry into village", "Remained in village boundary", "Became aggressive", "Others"};
    public static String[] HWC_ELEPHANT_ACTIVITY_REACTION_AFTER_ENTRY = {"Crop damage", "Grain/alcohol storage raided", "House/property damage", "Human casualty or injury", "Elephant casualty or injury", "Chased away without damage", "Walked away without causing any damage", "Any others"};


    public static String[] SPECIES_SURVEY_FOREST_NALA= {"Stream courses", "Nala", "Forest Trail", "Others"};
    public static String[] SPECIES_SURVEY_AGE_OF_SIGN= {"Old", "Fresh"};
    public static String[] SPECIES_SURVEY_CARNIVORE= {"बाघ", "चितुवा", "भूइ भालु", "हुँडार", "जंगली कुकुर", "बनबिरालो", "स्याल", "निरबिरालो", "न्यौरिमुसा", "अरु बिरालो प्रजाति"};
    public static String[] SPECIES_SURVEY_HERBIVORE= {"चित्तल", "लगुना", "बारासिंघा", "गौर", "साम्बर", "बँदेल", "रतुवा", "खरायो", "चौका", "बाँदर", "लंगुर", "दुम्सी", "हात्ती", "गाइबस्तु", "बाख्रा", "अपरिचित"};
    public static String[] SPECIES_SURVEY_SIGN= {"दिसा", "गोबर", "जनावरको पाइला", "कोतारेको निशान", "मारेको/खाएको संकेत", "अंग", "Others"};
    public static String[] SPECIES_SURVEY_FOREST= {"साल जंगल", "मिश्रित जंगल", "नदितटीए बन", "लामो घासेमैदान", "छोटो घासेमैदान", "सिमसार", "Others"};
    public static String[] SPECIES_SURVEY_TERRIAN= {"पहाडी", "तराई", "खोला किनार", "Others"};

    public static String[] FOREST_FIRE = {"Yes", "No"};
    public static String[] HUMAN_PRESENCE = {"Yes", "No"};

    public static String[] LIVESTOCK_TRAIL = {"Yes", "No"};
    public static String[] TYPES_OF_POACHING = {"Gun Shot", "Poacher(s) Camp", "Snares", "Poacher(s) Activities", "Others"};

    public static String[] HABITAT_TYPE = {"SF-Sal Forest",  "MF-Mixed Forest", "RF-Riverine Forest", "TG-Tall Grassland", "SG–Short Grassland", "W-Wetland", "S-Streambed"};

    public static String[] TERRAIN = {"Hilly", "Flat", "Streambed"};
    public static String[] SLOPE = {"0-30", "30-60", "60-90"};
    public static String[] ASPECT = {"NE", "NW", "SE", "SW"};

    public static String[] SPECIES_NAME = {"Grass", "Herbs and Seedling", "Bare soil, Rock and Boulders", "Leaf/litter"};

    public static String[] INVASIVE_SPP = {"0-25%", "25-50%", "50-75%", "75-100%"};
}
