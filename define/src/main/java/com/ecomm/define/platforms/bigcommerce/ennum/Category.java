package com.ecomm.define.platforms.bigcommerce.ennum;

/**
 * Created by vamshikirangullapelly on 04/05/2020.
 */
public enum Category {


    LIVING(27, "Living"),
    LIVING_SOFAS(28, "Sofa"),
    LIVING_ARMCHAIRS(29, "ArmChair"),
    LIVING_CHAIRS(29, "Chair"),
    LIVING_CHAISE(28, "Chaise"),
    LIVING_CHAISE_SOFA(29, "Chaise"),
    LIVING_SOFA_BEDS(30, "Sofa Bed"),
    LIVING_FOOTSTOOLS(31, "Foot Stool"),
    LIVING_TABLES(33, "Table"),
    LIVING_STORAGE(34, "Storage"),
    SOFAS_ARMCHAIRS(35, "ArmChair"),
    SA_SOFAS(36, "Sofa"),
    SA_ARMCHAIRS(37, "Armchair"),
    SA_CHAIRS(37, "Chair"),
    SA_VELVET(37, "Velvet Chair"),
    SA_SOFABEDS(39, "Sofa Bed"),
    SA_FOOTSTOOLS(40, "Foot Stool"),
    SA_STOOLS(40, "Stool"),
    KITCHEN_DINING(41, "Kitchen"),
    DINING(41, "Dining"),
    KD_DINING_TABLES(42, "Dining Table"),
    KD_DINING_TABLE_SETS(43, "Dining Table Set"),
    KD_DINING_TABLE_SET(43, "Dining Table + Chair"),
    KD_DINING_CHAIRS(44, "Dining Chair"),
    KD_STORAGE(45, "Storage"),
    STORAGE_BUFFET(45, "Buffet"),
    STORAGE_SIDEBOARD(45, "Sideboard"),
    BEDROOM_BATHROOM(46, "Bedroom"),
    BB_BEDS(47, "Bed"),
    BB_MATTRESSES(48, "Mattress"),
    BB_STORAGE(49, "Storage"),
    BB_ARMOIRE(50, "Armoire"),
    BB_WARDROBE(50, "Wardrobe"),
    BEDROOM_FURNITURE(50, "Bedroom"),
    BB_BEDROOM_FURNITURE(50, "Bathroom"),
    BB_TOILET_FURNITURE(50, "Toilet"),
    BB_TOWEL_FURNITURE(50, "Towel"),
    KIDS_ROOM(51, "Kid"),
    KR_BUNK_BEDS(52, "Bunk Bed"),
    KR_MID_SLEEPERS(53, "Mid Sleeper"),
    KR_KIDS_DAY_BEDS(54, "Day Bed"),
    KR_SINGLE_BEDS(55, "Single Bed"),
    KR_BEDS(55, "Kid Bed"),
    KR_FURNITURE(56, "Kid"),
    KR_DESKS(57, "Desk"),
    HOME_FURNISHINGS(58, "Furnishing"),
    CARPET(58, "Carpet"),
    HF_DECORATIONS(59, "Decoration"),
    HF_WING_DECORATIONS(59, "Wing"),
    HF_WINGS_DECORATIONS(59, "Wings"),
    HF_LANTERB(59, "Lantern"),
    HF_CURTAIN_HOLDER(59, "Curtain Holder"),
    HF_WALLLIGHT(59, "Wall Light"),
    HF_WALLSHELF(59, "Wall Shelf"),
    HF_CHURN(59, "Churn"),
    HF_STAGE(59, "Stag"),
    HF_SCULPTURE(59, "Sculpture"),
    HF_CANOPY(58, "Canopy"),
    HF_WALL_CANOPY(58, "Canopy"),
    HOME_OFFICE(60, "Home Office"),
    OFFICE(60, " Office"),
    HO_BOOKCASES(61, "Bookcase"),
    HO_CABINETS(62, "Cabinet"),
    HO_DESKS(63, "Desk"),
    HO_STORAGE(64, "Storage"),
    SALES(65, "Sale"),
    OFFERS(65, "Offers"),
    DECO_TILES(66, "Tile"),
    DECO_WALL_TILES(67, "Wall Tile"),
    HALLWAY(68, "Hallway"),
    FURNITURE(69, "Furniture (All)"),
    HALLWAY_CONSOLE_TABLES(70, "Console"),
    HF_DECORATIONS_MIRRORS(71, "Mirror"),
    FRENCH_FURNITURE(72, "French"),
    RUSTIC_FURNITURE(73, "Rustic"),
    DISTRESSED_FURNITURE(74, "Distressed"),
    HF_DECORATIONS_DOOR_KNOCKERS(75, "Door Knocker"),
    VINTAGE_FURNITURE(72, "Vintage"),
    GARDEN_FURNITURE(73, "Garden"),
    CHURN(73, "Churn"),
    GARDEN_CHURN(77, "Churn"),
    CHANDELIER(85, "Chandelier"),
    WALL_CLOCKS(86, "Wall Clock"),
    LAMP_SHADE(84, "Lampshade"),
    LIGHT_SHADE(84, "Lightshade"),
    MIRRORS(71, "Mirror"),
    VENETIAN_FURNITURE(83, "Venetian"),
    FIRE_SORROUNDS(82, "Fire Surround"),
    BUFFET_TABLE(81, "Buffet"),
    BUFFET_STORAGE(81, "Sideboard"),
    SIDEBOARD(80, "Sideboard"),
    LIVING_TABLES_COFFEE_TABLES(78, "Coffee Table"),
    CANDLE_HOLDER(88,"Candle Holder"),
    CANDELABRA(88,"Candelabra"),
    ALUMINIUM(89,"Aluminium"),
    BEDSIDES(90,"Bedside"),
    CHEST_OF_DRAWERS(91,"Chest"),
    ART_SUPPLIES(92,"Easel"),
    ART_STAND(92,"Stand");
    private final int categoryCode;
    private final String categoryWord;

    Category(int categoryCode, String categoryWord) {
        this.categoryCode = categoryCode;
        this.categoryWord = categoryWord;
    }

    public int getCategoryCode() {
        return this.categoryCode;
    }

    public String getCategoryWord() {
        return this.categoryWord;
    }
}

