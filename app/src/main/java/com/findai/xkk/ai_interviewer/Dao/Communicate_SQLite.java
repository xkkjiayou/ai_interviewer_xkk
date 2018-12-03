package com.findai.xkk.ai_interviewer.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Communicate_SQLite extends SQLiteOpenHelper {

    public Communicate_SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db){
        //创建表SQL语句
        //type 1：问答题  type 0：选择题
        String question_table="CREATE TABLE question(qid integer primary key ,qtitle text,answer text,type integer)";
        String question_choose_table="CREATE TABLE question_choose_items(qid integer ,item text)";

        //执行SQL语句
        db.execSQL(question_table);
        db.execSQL(question_choose_table);

    }


    public void insert(SQLiteDatabase db,String sql){
        //插入数据SQL语句
//        String stu_sql="insert into question(qid,qtitle,answer,type) values('xiaoming','01005')";

        //执行SQL语句
        db.execSQL(sql);
    }

    public void delete(SQLiteDatabase db,String sql) {
        //删除SQL语句
        //String sql = "delete from stu_table where _id = 6";
        //执行SQL语句
        db.execSQL(sql);
    }

    public void update(SQLiteDatabase db,String sql){
        //修改SQL语句
//        String sql = "update stu_table set snumber = 654321 where id = 1";
        //执行SQL
        db.execSQL(sql);
    }

    public void query(SQLiteDatabase db,String sql) {
        //查询获得游标
        Cursor cursor = db.rawQuery(sql,null);

        //判断游标是否为空
        if(cursor.moveToFirst())
        {
                //遍历游标
            for(int i=0;i<cursor.getCount();i++){
                cursor.move(i);
                //获得ID
                int id = cursor.getInt(0);
                //获得用户名
                String username=cursor.getString(1);
                //获得密码
                String password=cursor.getString(2);
                //输出用户信息
//                System.out.println(id+":"+sname+":"+snumber);
            }
        }
    }


}
