package com.example.service;

import com.example.database.JsonDatabase;
import com.example.model.Bill;

import java.util.List;

public class BillService {

    private List<Bill> bills;

    public BillService(){
        bills = JsonDatabase.loadBills();
    }

    public List<Bill> getBills(){
        return bills;
    }

    public void addBill(Bill bill){

        bills.add(bill);

        JsonDatabase.saveBills(bills);

    }
}