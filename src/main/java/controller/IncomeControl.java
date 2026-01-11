package controller;

import dataflow.DataManager;
import model.Transaksi;
import java.util.ArrayList;

public class IncomeControl extends AbstractRecordControl {

    @Override
    protected void setTipeRecord() {
        this.tipeRecord = "income";
    }

    @Override
    protected ArrayList<Transaksi> getDataTransaksi() {
        this.tipeRecord = "income";
        return DataManager.getInstance().getDataTransaksiPemasukan();
    }
}