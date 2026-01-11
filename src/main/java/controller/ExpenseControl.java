package controller;

import dataflow.DataManager;
import model.Transaksi;
import java.util.ArrayList;

public class ExpenseControl extends AbstractRecordControl {

    @Override
    protected void setTipeRecord() {
        this.tipeRecord = "expense";
    }

    @Override
    protected ArrayList<Transaksi> getDataTransaksi() {
        return DataManager.getInstance().getDataTransaksiPengeluaran();
    }
}