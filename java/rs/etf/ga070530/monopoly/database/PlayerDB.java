package rs.etf.ga070530.monopoly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ga070530.monopoly.Model.Player;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.PlayerTable;
import rs.etf.ga070530.monopoly.database.MonopolyDBSchema.PlayerTable.PlayerCols;

public class PlayerDB {

    public static long setPlayer(Context context, int id, String name, String type, int money) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PlayerCols.ID, id);
        values.put(PlayerCols.NAME, name);
        values.put(PlayerCols.TYPE, type);
        values.put(PlayerCols.MONEY, money);

        long results = db.insert(MonopolyDBSchema.PlayerTable.PLAYER, MonopolyDBSchema.PlayerTable.PLAYER, values);
        db.close();
        return results;
    }

    public static int getPlayer(Context context, int id) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Player player = null;
        Cursor c = null;

        switch (id) {
            case 1:
                c = db.rawQuery("SELECT * FROM " + PlayerTable.PLAYER +
                        " WHERE " + PlayerCols.ID + " = " + id, null);
                break;
            case 2:
                c = db.rawQuery("SELECT * FROM " + PlayerTable.PLAYER +
                        " WHERE " + PlayerCols.ID + " = " + id, null);
                break;
            case 3:
                c = db.rawQuery("SELECT * FROM " + PlayerTable.PLAYER +
                        " WHERE " + PlayerCols.ID + " = " + id, null);
                break;
            case 4:
                c = db.rawQuery("SELECT * FROM " + PlayerTable.PLAYER +
                        " WHERE " + PlayerCols.ID + " = " + id, null);
                break;
        }

        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(PlayerCols.ID));
//            String name = c.getString(c.getColumnIndex(PlayerCols.NAME));
//            String type = c.getString(c.getColumnIndex(PlayerCols.TYPE));
//            int money = c.getInt(c.getColumnIndex(PlayerCols.MONEY));
//
//            player = new Player(context, id, name, type, money);
        }
        c.close();
        db.close();

        return id;
    }

    public static int getMoney(Context context, int id) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int money = 0;

        Cursor c = db.rawQuery("SELECT " + PlayerCols.MONEY + " FROM " + PlayerTable.PLAYER + " WHERE " + PlayerCols.ID + " = " + id, null);


        db.beginTransaction();
        while (c.moveToNext()) {
            money = c.getInt(c.getColumnIndex(PlayerCols.MONEY));
        }
        c.close();
        db.endTransaction();
        db.close();
        return money;
    }

    public static List<Player> getPlayers(Context context, int id) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        List<Player> players = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + PlayerTable.PLAYER + " WHERE " + PlayerCols.ID + " != " + id + " ORDER BY " + PlayerCols.ID + " ASC", null);


        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(PlayerCols.ID));
            String name = c.getString(c.getColumnIndex(PlayerCols.NAME));
            String type = c.getString(c.getColumnIndex(PlayerCols.TYPE));
            int money = c.getInt(c.getColumnIndex(PlayerCols.MONEY));

            players.add(new Player(context, id, name, type, money));
        }
        c.close();
        db.close();

        return players;
    }

    public static void setMoney(Context context, int id, int money) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        String stringMoney = Integer.toString(money);
        String stringId = Integer.toString(id);

        Cursor c = db.rawQuery("UPDATE " + PlayerTable.PLAYER + " SET " + PlayerCols.MONEY + " = " + stringMoney
                + " WHERE " + PlayerCols.ID + " = " + stringId, null);
        c.moveToFirst();
        c.close();
        db.close();
    }

    public static void reinitiatePlayers(Context context) {
        MonopolyOpenHelper openHelper = new MonopolyOpenHelper(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS '" + PlayerTable.PLAYER + "'");

        db.execSQL("CREATE TABLE " + PlayerTable.PLAYER + "(" +
                PlayerCols.ID + " INTEGER PRIMARY KEY, " +
                PlayerCols.NAME + ", " +
                PlayerCols.TYPE + ", " +
                PlayerCols.MONEY + ")");
        db.close();
    }
}
