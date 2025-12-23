package service;

import dataflow.DataManager;
import model.Transaksi;

public class IncomeManager implements TransactionManager{
    public static int incomeSum() {
        int sum = 0;
        for(Transaksi trans : DataManager.getInstance().getDataTransaksi()){
            sum += trans.getJumlah();
        }
        return sum;
    }
}
