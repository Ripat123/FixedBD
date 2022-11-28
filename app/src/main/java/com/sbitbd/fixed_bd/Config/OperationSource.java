package com.sbitbd.fixed_bd.Config;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperationSource {
    public Boolean insert(SQLiteDatabase DB, ContentValues contentValues, String table){
        long result = DB.insert(table,null,contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean update(SQLiteDatabase DB, ContentValues contentValues,String table,String where){
        long result = DB.update(table,contentValues,where,null);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Boolean delete(SQLiteDatabase DB,String table,String where){
        long result = DB.delete(table,where,null);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getData(SQLiteDatabase db, String sql){
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
}
