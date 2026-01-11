package controller;

import dataflow.DataManager;
import model.Transaction;

import java.util.ArrayList;

public class IncomeControl extends AbstractRecordControl {

    @Override
    protected void setTipeRecord() {
        this.recordType = "income";
    }

    @Override
    protected ArrayList<Transaction> getDataTransaksi() {
        this.recordType = "income";
        return DataManager.getInstance().getIncomeTransactionData();
    }
}