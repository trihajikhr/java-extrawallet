package controller;

import dataflow.DataManager;
import model.Transaction;
import java.util.ArrayList;

public class ExpenseControl extends AbstractRecordControl {

    @Override
    protected void setTipeRecord() {
        this.recordType = "expense";
    }

    @Override
    protected ArrayList<Transaction> getDataTransaksi() {
        return DataManager.getInstance().getExpenseTransactionData();
    }
}