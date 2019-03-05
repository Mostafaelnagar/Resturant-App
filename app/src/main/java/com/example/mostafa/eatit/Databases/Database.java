package com.example.mostafa.eatit.Databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.mostafa.eatit.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafa on 9/30/2017.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EatItDb.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //select from OrderDetain
    public List<Order> getCarts() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qp = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductId","ProductName","Quantity","Price","Discount"};
        String sqlTable = "OrderDetail";
        qp.setTables(sqlTable);
        Cursor c = qp.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))


                ));
            } while (c.moveToNext());
        }
        return result;
    }

    //Insert into orderDeatail
    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (ProductId,ProductName,Quantity,Price,Discount) VALUES ('%S','%S','%S','%S','%S'); ",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()
        );
        db.execSQL(query);

    }
    //delete from orderDeatail
    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }

    public void addFavorites(String FoodId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO favorites (FoodId) VALUES('%S');",FoodId);
        db.execSQL(query);
    }
    public void removeFromFavorites(String FoodId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM favorites  WHERE FoodId = '%S';",FoodId);
        db.execSQL(query);
    }
    public boolean isFavorites(String FoodId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM favorites WHERE FoodId ='%S';",FoodId);
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }
}
