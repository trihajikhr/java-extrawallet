package dataflow;

import model.Pengeluaran;
import model.Pemasukan;

import java.util.ArrayList;

public class DataManager {
    private static DataManager instance;
    private ArrayList<Pemasukan> income = new ArrayList<Pemasukan>();
    private ArrayList<Pengeluaran> expense = new ArrayList<Pengeluaran>();

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


}
