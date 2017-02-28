package rs.etf.ga070530.monopoly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Card;
import rs.etf.ga070530.monopoly.Model.CardGroups;
import rs.etf.ga070530.monopoly.R;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardGroupsTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardGroupsTable.GroupCols;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardsTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.PlayerCards;

public class CardsDB {

    public static List<Card> initCards(Context context) {

        Card[] mCards = new Card[]{
                new Card(0, context.getResources().getString(R.string.start), 10, 755, 0, null, false, false, 0),

                new Card(1, context.getResources().getString(R.string.gdynia), 10, 655, 600, null, false, true, 0),
                new Card(2, context.getResources().getString(R.string.community_chest), 10, 590, 0, null, false, false, 0),
                new Card(3, context.getResources().getString(R.string.taipei), 10, 525, 600, null, false, true, 0),
                new Card(4, context.getResources().getString(R.string.income_tax), 10, 460, 2000, null, false, false, 0),
                new Card(5, context.getResources().getString(R.string.rail), 10, 395, 2000, null, false, true, 0),
                new Card(6, context.getResources().getString(R.string.tokyo), 10, 330, 1000, null, false, true, 0),
                new Card(7, context.getResources().getString(R.string.chance), 10, 265, 0, null, false, false, 0),
                new Card(8, context.getResources().getString(R.string.barcelona), 10, 200, 1200, null, false, true, 0),
                new Card(9, context.getResources().getString(R.string.athens), 10, 135, 1200, null, false, true, 0),

                new Card(10, context.getResources().getString(R.string.jail_visit), 10, 45, 0, null, false, false, 0),

                new Card(11, context.getResources().getString(R.string.istanbul), 105, 45, 1400, null, false, true, 0),
                new Card(12, context.getResources().getString(R.string.solar_energy), 175, 45, 1500, null, false, true, 0),
                new Card(13, context.getResources().getString(R.string.kyiv), 240, 45, 1400, null, false, true, 0),
                new Card(14, context.getResources().getString(R.string.toronto), 305, 45, 1600, null, false, true, 0),
                new Card(15, context.getResources().getString(R.string.air), 370, 45, 2000, null, false, true, 0),
                new Card(16, context.getResources().getString(R.string.rome), 435, 45, 1800, null, false, true, 0),
                new Card(17, context.getResources().getString(R.string.community_chest), 500, 45, 0, null, false, false, 0),
                new Card(18, context.getResources().getString(R.string.shanghai), 565, 45, 1800, null, false, true, 0),
                new Card(19, context.getResources().getString(R.string.vancouver), 630, 45, 2000, null, false, true, 0),

                new Card(20, context.getResources().getString(R.string.parking), 730, 45, 0, null, false, false, 0),

                new Card(21, context.getResources().getString(R.string.sydney), 730, 140, 2200, null, false, true, 0),
                new Card(22, context.getResources().getString(R.string.chance), 730, 205, 0, null, false, false, 0),
                new Card(23, context.getResources().getString(R.string.new_york), 730, 270, 2200, null, false, true, 0),
                new Card(24, context.getResources().getString(R.string.london), 730, 335, 2400, null, false, true, 0),
                new Card(25, context.getResources().getString(R.string.cruise), 730, 400, 2000, null, false, true, 0),
                new Card(26, context.getResources().getString(R.string.beijing), 730, 465, 2600, null, false, true, 0),
                new Card(27, context.getResources().getString(R.string.hong_kong), 730, 530, 2600, null, false, true, 0),
                new Card(28, context.getResources().getString(R.string.wind_energy), 730, 595, 1500, null, false, true, 0),
                new Card(29, context.getResources().getString(R.string.jerusalem), 730, 660, 2800, null, false, true, 0),

                new Card(30, context.getResources().getString(R.string.jail), 730, 755, 0, null, false, false, 0),

                new Card(31, context.getResources().getString(R.string.paris), 630, 755, 3000, null, false, true, 0),
                new Card(32, context.getResources().getString(R.string.belgrade), 565, 755, 3000, null, false, true, 0),
                new Card(33, context.getResources().getString(R.string.community_chest), 500, 755, 0, null, false, false, 0),
                new Card(34, context.getResources().getString(R.string.cape_town), 435, 755, 3200, null, false, true, 0),
                new Card(35, context.getResources().getString(R.string.space), 370, 755, 2000, null, false, true, 0),
                new Card(36, context.getResources().getString(R.string.chance), 305, 755, 0, null, false, false, 0),
                new Card(37, context.getResources().getString(R.string.riga), 240, 755, 3500, null, false, true, 0),
                new Card(38, context.getResources().getString(R.string.super_tax), 175, 755, 1000, null, false, false, 0),
                new Card(39, context.getResources().getString(R.string.montreal), 105, 755, 4000, null, false, true, 0)
        };


        for (int i = 0; i < mCards.length; i++) {
            setCards(context, mCards[i].getNumberOfField(), mCards[i].getNameOfField(), mCards[i].getX(),
                    mCards[i].getY(), mCards[i].getPrice(), mCards[i].getIsBuyable(), mCards[i].getHouses());
        }
        return getCards(context);
    }

    public static void initGroups(Context context) {
        List<CardGroups> groups = new ArrayList<>();
        groups.add(new CardGroups(0, 1, 3));
        groups.add(new CardGroups(1, 6, 8, 9));
        groups.add(new CardGroups(2, 11, 13, 14));
        groups.add(new CardGroups(3, 16, 18, 19));
        groups.add(new CardGroups(4, 21, 23, 24));
        groups.add(new CardGroups(5, 26, 27, 29));
        groups.add(new CardGroups(6, 31, 32, 34));
        groups.add(new CardGroups(7, 37, 39));
        groups.add(new CardGroups(8, 5, 15, 25, 35));
        groups.add(new CardGroups(9, 12, 28));

        for (int i = 0; i < groups.size(); i++) {
            CardGroups cardGroup = groups.get(i);
            setGroupCards(context, cardGroup.getId(), cardGroup.getCard1(),
                    cardGroup.getCard2(), cardGroup.getCard3(), cardGroup.getCard4());
        }

    }


    public static long setGroupCards(Context context, int id, int card1, int card2, int card3, int card4) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(GroupCols.ID, id);
        values.put(GroupCols.CARD_1, card1);
        values.put(GroupCols.CARD_2, card2);
        values.put(GroupCols.CARD_3, card3);
        values.put(GroupCols.CARD_4, card4);

        long results = db.insert(CardGroupsTable.CARD_GROUPS, CardGroupsTable.CARD_GROUPS, values);
        db.close();
        return results;
    }


    public static long setCards(Context context, int noOfField, String name, float x, float y, int price, boolean isBuyable, int houses) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(CardsTable.CCols.ID, noOfField);
        values.put(CardsTable.CCols.NAME, name);
        values.put(CardsTable.CCols.X_COORD, x);
        values.put(CardsTable.CCols.Y_COORD, y);
        values.put(CardsTable.CCols.PRICE, price);
        values.put(CardsTable.CCols.BUYABLE, isBuyable);
        values.put(CardsTable.CCols.HOUSES, houses);

        long results = db.insert(CardsTable.CARDS_TABLE, CardsTable.CARDS_TABLE, values);
        db.close();
        return results;
    }

    public static List<Card> getCards(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<Card> cards = new ArrayList<>();

        int noOfField = 0, price = 0, houses = 0;
        float x = 0, y = 0;
        boolean isBuyable = false;
        String nameOfField = null;

//        Cursor c = db.rawQuery("SELECT * FROM " + MonopolyDBSchema.CardsTable.CARDS_TABLE + " ORDER BY " + MonopolyDBSchema.CardsTable.CCols.ID + " ASC", null);
        Cursor c = db.query(CardsTable.CARDS_TABLE,
                new String[]{CardsTable.CCols.ID, CardsTable.CCols.NAME,
                        CardsTable.CCols.X_COORD, CardsTable.CCols.Y_COORD,
                        CardsTable.CCols.PRICE, CardsTable.CCols.BUYABLE, CardsTable.CCols.HOUSES},
                null, null, null, null, null);

        while (c.moveToNext()) {
            noOfField = c.getInt(c.getColumnIndex(CardsTable.CCols.ID));
            nameOfField = c.getString(c.getColumnIndex(CardsTable.CCols.NAME));
            x = c.getFloat(c.getColumnIndex(CardsTable.CCols.X_COORD));
            y = c.getFloat(c.getColumnIndex(CardsTable.CCols.Y_COORD));
            price = c.getInt(c.getColumnIndex(CardsTable.CCols.PRICE));
            isBuyable = c.getInt(c.getColumnIndex(CardsTable.CCols.BUYABLE)) > 0;
            houses = c.getInt(c.getColumnIndex(CardsTable.CCols.HOUSES));

            Card card = new Card(noOfField, nameOfField, x, y, price, null, false, isBuyable, houses);
            cards.add(card);
        }
        c.close();
        db.close();

        return cards;
    }

    public static void deleteCards(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + CardsTable.CARDS_TABLE);
    }

    public static void transferCards(Context context, int id, int oId, int cardId) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        String stringId = Integer.toString(id);
        String stringOid = Integer.toString(oId);
        String stringCid = Integer.toString(cardId);

        Cursor c = db.rawQuery("UPDATE " + PlayerCards.PLAYER_CARDS + " SET " + PlayerCards.Cards.PLAYER_ID + " = " + stringOid
                + " WHERE " + PlayerCards.Cards.PLAYER_ID + " = " + stringId + " AND " + PlayerCards.Cards.CARD_ID + " = " + stringCid, null);

        c.moveToFirst();
        while (c.moveToNext()) {
            c.moveToNext();

        }
//        c.moveToFirst();
        c.close();
    }

    public static void deletePlayerCards(Context context, int id) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();


         String query = "DELETE FROM " + PlayerCards.PLAYER_CARDS + " WHERE " + PlayerCards.Cards.PLAYER_ID + " = " + id;

        Cursor c = db.rawQuery("SELECT * FROM " + PlayerCards.PLAYER_CARDS +
                " WHERE " + PlayerCards.Cards.PLAYER_ID + " = " + id, null) ;

        try {
            while (c.moveToNext()){
                db.execSQL(query);
            }
        }catch (Exception e){

        }finally {
            c.close();
            db.close();
        }
    }

    public static void deleteSpecPlayerCards(Context context, int playerId, int cardId) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();


        String query = "DELETE FROM " + PlayerCards.PLAYER_CARDS + " WHERE " +
                PlayerCards.Cards.PLAYER_ID + " = " + playerId + " AND " +
                PlayerCards.Cards.CARD_ID + " = " + cardId;

        try {
                db.execSQL(query);

        }catch (Exception e){

        }finally {
            db.close();
        }
    }

    public static long insertPlayerCard(Context context, int pId, int cardId) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(PlayerCards.Cards.PLAYER_ID, pId);
        values.put(PlayerCards.Cards.CARD_ID, cardId);

        long result = db.insert(PlayerCards.PLAYER_CARDS, null, values);
        db.close();
        return result;
    }


    public static List<Integer> getPlayerCards(Context context, int pId) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<Integer> cards = new ArrayList<>();

        int id = 0;
        Cursor c = db.rawQuery("SELECT " + PlayerCards.Cards.CARD_ID + " FROM " +
                PlayerCards.PLAYER_CARDS + " WHERE " + PlayerCards.Cards.PLAYER_ID + " = " +
                pId + " ORDER BY " + PlayerCards.Cards.CARD_ID + " ASC", null);

        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(PlayerCards.Cards.CARD_ID));

            cards.add(id);
        }
        c.close();
        db.close();
        return cards;
    }

    public static int getOwner(Context context, int cardId) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int playerId = 0;

        Cursor c = db.rawQuery("SELECT " + PlayerCards.Cards.PLAYER_ID + " FROM " + PlayerCards.PLAYER_CARDS +
                " WHERE " + PlayerCards.Cards.CARD_ID + " = " + cardId, null);

        while (c.moveToNext()) {
            playerId = c.getInt(c.getColumnIndex(PlayerCards.Cards.PLAYER_ID));
        }
        c.close();

        return playerId;
    }

    public static void reinitiatePCards(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS '" + PlayerCards.PLAYER_CARDS + "'");
        db.execSQL("CREATE TABLE " + PlayerCards.PLAYER_CARDS + "(" +
                PlayerCards.Cards.PLAYER_ID + ", " +
                PlayerCards.Cards.CARD_ID + ")");
        db.close();
    }
}
