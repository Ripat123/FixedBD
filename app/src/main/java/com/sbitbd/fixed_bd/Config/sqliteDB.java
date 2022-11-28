package com.sbitbd.fixed_bd.Config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqliteDB extends SQLiteOpenHelper {
    SQLiteDatabase DB = this.getWritableDatabase();
    OperationSource query = new OperationSource();

    public sqliteDB(@Nullable Context context) {
        super(context, "sbitcom_multivendor.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create Table guest(id INTEGER,guest_id VARCHAR,name VARCHAR," +
                    "phone VARCHAR,email VARCHAR,password VARCHAR)");
            db.execSQL("create Table seller(id INTEGER,seller_id VARCHAR,name VARCHAR," +
                    "phone VARCHAR,email VARCHAR,password VARCHAR)");
            db.execSQL("create Table shopping_carts(id INTEGER PRIMARY KEY AUTOINCREMENT,product_id BIGINT NOT NULL," +
                    "session_id VARCHAR NOT NULL,status INTEGER NOT NULL,quantity INTEGER NOT NULL,min_quantity INTEGER,stock_quantity INTEGER," +
                    "color VARCHAR,size VARCHAR,created_at VARCHAR,updated_at VARCHAR)");
            db.execSQL("create Table session(id INTEGER,session_data VARCHAR NOT NULL)");
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists guest");
        db.execSQL("drop Table if exists seller");
        db.execSQL("drop Table if exists shopping_carts");
        db.execSQL("drop Table if exists session");
        onCreate(db);
    }

    public Boolean DataOperation(ContentValues contentValues, String choose, String table, String where){
        if(choose.equals("insert")){
            return query.insert(DB,contentValues,table);
        }else if(choose.equals("update")){
            return query.update(DB,contentValues,table,where);
        }else if(choose.equals("delete")){
            return query.delete(DB,table,where);
        }else
            return false;
    }
    public Cursor getUerData(String sql){
        return query.getData(DB,sql);
    }
}
