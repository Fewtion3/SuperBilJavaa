package com.example.database;

import com.example.model.Bill;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDatabase {

    private static final String FILE = "database.json";

    private static Gson gson = new Gson();

    // โหลดข้อมูลจาก JSON
    public static List<Bill> loadBills(){

        try{

            FileReader reader = new FileReader(FILE);

            Type listType = new TypeToken<List<Bill>>(){}.getType();

            List<Bill> bills = gson.fromJson(reader,listType);

            reader.close();

            return bills;

        }catch(Exception e){

            return new ArrayList<>();

        }

    }

    // บันทึกข้อมูลลง JSON
    public static void saveBills(List<Bill> bills){

        try{

            FileWriter writer = new FileWriter(FILE);

            gson.toJson(bills,writer);

            writer.close();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}