package com.example.service;

import java.util.List;

import com.example.database.JsonDatabase;
import com.example.model.Bill;

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

    public void deleteBill(Bill bill) {
        bills.remove(bill);
        JsonDatabase.saveBills(bills);
    }

}