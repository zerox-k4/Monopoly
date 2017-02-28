package rs.etf.ga070530.monopoly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Settings;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardGroupsTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardGroupsTable.GroupCols;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardsTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.PlayerCards;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.PlayerTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.SettingsTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.SettingsTable.Cols;

import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.CardsTable.*;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.StatisticTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.StatisticTable.StatisticCols;


public class MonopolyOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "monopoly.db";

    public MonopolyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + SettingsTable.NAME + "(" +
                Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.INIT_CASH + ", " +
                Cols.GO_PASS + ", " +
                Cols.GO_LAND + ")");

//        db.execSQL("CREATE TABLE " + PlayerTable.PLAYER + "(" +
//                PlayerCols.ID + " INTEGER PRIMARY KEY, " +
//                PlayerCols.NAME + ", " +
//                PlayerCols.TYPE + ", " +
//                PlayerCols.MONEY +")");

        db.execSQL("CREATE TABLE " + CardsTable.CARDS_TABLE + "(" +
                CCols.ID + " INTEGER PRIMARY KEY, " +
                CCols.NAME + ", " +
                CCols.X_COORD + ", " +
                CCols.Y_COORD + ", " +
                CCols.PRICE + ", " +
//                CCols.OWNER + ", " +
//                CCols.MORTGAGED + ", " +
                CCols.BUYABLE + ", " +
                CCols.HOUSES + ")");
        db.execSQL("DROP TABLE IF EXISTS '" + CardGroupsTable.CARD_GROUPS + "'");
        db.execSQL("CREATE TABLE " + CardGroupsTable.CARD_GROUPS + "(" +
                GroupCols.ID + " INTEGER PRIMARY KEY, " +
                GroupCols.CARD_1 + ", " +
                GroupCols.CARD_2 + ", " +
                GroupCols.CARD_3 + ", " +
                GroupCols.CARD_4 + ")");

        db.execSQL("CREATE TABLE " + StatisticTable.STATISTIC_NAME + "(" +
                StatisticCols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StatisticCols.GAME_ID + ", " +
                StatisticCols.PLAYERS + ", " +
                StatisticCols.MOVE + ", " +
                StatisticCols.START_TIME + ", " +
                StatisticCols.DURATION + ", " +
                StatisticCols.WINNER + ", " +
                StatisticCols.MONEY + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist '" + PlayerTable.PLAYER + "'");
        db.execSQL("drop table if exist '" + SettingsTable.NAME + "'");
        db.execSQL("drop table if exist '" + CardsTable.CARDS_TABLE + "'");
        db.execSQL("drop table if exist '" + PlayerCards.PLAYER_CARDS + "'");
        db.execSQL("drop table if exist '" + CardGroupsTable.CARD_GROUPS + "'");
        db.execSQL("drop table if exist '" + StatisticTable.STATISTIC_NAME + "'");
        onCreate(db);
    }

    public static int setId(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        Cursor c = null;

        c = db.rawQuery("SELECT MAX (" + StatisticCols.GAME_ID + ") FROM " + StatisticTable.STATISTIC_NAME, null);
        c.moveToFirst();

        try {
            int id = c.getInt(0);

            return id + 1;
        } catch (Exception e) {
            return 0;
        } finally {
            c.close();
            db.close();
        }


    }


    public static long initStatistic(Context context, int game_id, String player, long startTime) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StatisticCols.GAME_ID, game_id);
        values.put(StatisticCols.PLAYERS, player);
        values.put(StatisticCols.START_TIME, startTime);


        long result = db.insert(StatisticTable.STATISTIC_NAME, StatisticTable.STATISTIC_NAME, values);
        db.close();
        return result;
    }

    public static long addMove(Context context, int game_id, String move) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StatisticCols.GAME_ID, game_id);
        values.put(StatisticCols.MOVE, move);


        long result = db.insert(StatisticTable.STATISTIC_NAME, StatisticTable.STATISTIC_NAME, values);
        db.close();
        return result;
    }

    public static long addWinner(Context context, int game_id, String winner, long duration, int money) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StatisticCols.GAME_ID, game_id);
        values.put(StatisticCols.WINNER, winner);
        values.put(StatisticCols.DURATION, duration);
        values.put(StatisticCols.MONEY, money);

        long result = db.insert(StatisticTable.STATISTIC_NAME, StatisticTable.STATISTIC_NAME, values);
        db.close();
        return result;
    }

    public static List<String> getStatistic(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<String> statistic = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + StatisticTable.STATISTIC_NAME +
                " WHERE " + StatisticCols.WINNER + " IS NOT NULL AND " + StatisticCols.WINNER + " != '' " +
                " ORDER BY " + StatisticCols.MONEY + " DESC, " + StatisticCols.GAME_ID + " DESC", null);

//        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex(StatisticCols.WINNER));
                int iMoney = c.getInt(c.getColumnIndex(StatisticCols.MONEY));
                String money = Integer.toString(iMoney);
                try {
                    statistic.add(name + " - " + money);
                } catch (Exception e) {
                    return null;
                }
            }
        db.close();
        return statistic;
    }

    public static void clearStatistic(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        db.execSQL("drop table if exists '" + StatisticTable.STATISTIC_NAME + "'");
        db.execSQL("CREATE TABLE " + StatisticTable.STATISTIC_NAME + "(" +
                StatisticCols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StatisticCols.GAME_ID + ", " +
                StatisticCols.PLAYERS + ", " +
                StatisticCols.MOVE + ", " +
                StatisticCols.START_TIME + ", " +
                StatisticCols.DURATION + ", " +
                StatisticCols.WINNER + ", " +
                StatisticCols.MONEY + ")");
        db.close();
    }


    public static long insertSettings(Context context, int initCash, int goPass, int goLand) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Cols.INIT_CASH, initCash);
        values.put(Cols.GO_PASS, goPass);
        values.put(Cols.GO_LAND, goLand);

        long result = db.insert(SettingsTable.NAME, SettingsTable.NAME, values);
        db.close();
        return result;
    }

    public static Settings getSettings(Context context) {
        int id = 0, init_cash = 0, go_pass = 0, go_land = 0;
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
//        Cursor c = db.query(SettingsTable.NAME,
//                new String[]{Cols.ID, Cols.INIT_CASH, Cols.GO_PASS, Cols.GO_LAND},
//                null, null, null, null, null);

        Cursor c = db.rawQuery("SELECT * FROM " + SettingsTable.NAME + " ORDER BY " + Cols.ID + " DESC LIMIT 1", null);

        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(Cols.ID));
            init_cash = c.getInt(c.getColumnIndex(Cols.INIT_CASH));
            go_pass = c.getInt(c.getColumnIndex(Cols.GO_PASS));
            go_land = c.getInt(c.getColumnIndex(Cols.GO_LAND));
            c.close();
        }
        c.close();
        Settings settings = new Settings(id, init_cash, go_pass, go_land);
        return settings;
    }

    public static boolean chkSettings(Context context) {
        SQLiteOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + SettingsTable.NAME + " ORDER BY " + Cols.ID + " ASC", null);

        if (!c.moveToNext()) {
            return false;
        }
        c.close();
        return true;
    }

    public static boolean chkCards(Context context) {
        SQLiteOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + CardsTable.CARDS_TABLE + " ORDER BY " + CardsTable.CCols.ID + " ASC", null);

        if (c.moveToNext()) {
            return false;
        }
        c.close();
        db.close();
        return true;
    }
}
