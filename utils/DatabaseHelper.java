package com.example.xxxmes.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mes.db";
    private static final int DATABASE_VERSION = 1;

    // 建表SQL语句
    private static final String SQL_CREATE_EMPLOYEE =
            "CREATE TABLE Employee (" +
                    "employeeId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "position TEXT," +
                    "password TEXT," +
                    "experience INTEGER," +
                    "processId INTEGER)";

    private static final String SQL_CREATE_EQUIPMENT =
            "CREATE TABLE Equipment (" +
                    "equipmentId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "status TEXT," +
                    "qrCode TEXT)";

    private static final String SQL_CREATE_MATERIAL =
            "CREATE TABLE Material (" +
                    "materialId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "type TEXT," +
                    "unitPrice REAL," +
                    "employeeId INTEGER," +
                    "totalQuantity INTEGER," +
                    "receivedQuantity INTEGER)";

    private static final String SQL_CREATE_PROCESS =
            "CREATE TABLE Process (" +
                    "processId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "equipmentId INTEGER," +
                    "description TEXT," +
                    "productId INTEGER)";

    private static final String SQL_CREATE_PROCESS_HAND_OVER =
            "CREATE TABLE ProcessHandOver (" +
                    "handOverId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sourceProcessId INTEGER," +
                    "targetProcessId INTEGER," +
                    "handOverDate TEXT," +
                    "quantity INTEGER," +
                    "status TEXT," +
                    "operatorId INTEGER," +
                    "remarks TEXT," +
                    "productId INTEGER)";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE Product (" +
                    "productId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "specifications TEXT," +
                    "material TEXT," +
                    "unitWeight REAL)";

    private static final String SQL_CREATE_PRODUCTION_TASK =
            "CREATE TABLE ProductionTask (" +
                    "taskId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "productId INTEGER," +
                    "batchNo TEXT," +
                    "status TEXT," +
                    "startTime TEXT," +
                    "endTime TEXT," +
                    "productName TEXT," +
                    "specifications TEXT," +
                    "material TEXT," +
                    "unitWeight REAL)";

    private static final String SQL_CREATE_REPORT =
            "CREATE TABLE Report (" +
                    "reportID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "productID INTEGER," +
                    "processID INTEGER," +
                    "taskID INTEGER," +
                    "workerName TEXT," +
                    "stationName TEXT," +
                    "productName TEXT," +
                    "specifications TEXT," +
                    "material TEXT," +
                    "unitWeight REAL," +
                    "inputQuantity INTEGER," +
                    "outputQuantity INTEGER," +
                    "scrapQuantity INTEGER," +
                    "remarks TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建所有表
        db.execSQL(SQL_CREATE_EMPLOYEE);
        db.execSQL(SQL_CREATE_EQUIPMENT);
        db.execSQL(SQL_CREATE_MATERIAL);
        db.execSQL(SQL_CREATE_PROCESS);
        db.execSQL(SQL_CREATE_PROCESS_HAND_OVER);
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_PRODUCTION_TASK);
        db.execSQL(SQL_CREATE_REPORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 简单处理：删除旧表，创建新表
        db.execSQL("DROP TABLE IF EXISTS Employee");
        db.execSQL("DROP TABLE IF EXISTS Equipment");
        db.execSQL("DROP TABLE IF EXISTS Material");
        db.execSQL("DROP TABLE IF EXISTS Process");
        db.execSQL("DROP TABLE IF EXISTS ProcessHandOver");
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS ProductionTask");
        db.execSQL("DROP TABLE IF EXISTS Report");
        onCreate(db);
    }
}