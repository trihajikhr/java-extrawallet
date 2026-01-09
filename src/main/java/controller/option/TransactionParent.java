package controller.option;

import model.RecordCard;
import model.Transaksi;

import java.util.Map;

public interface TransactionParent {
    Map<Transaksi, RecordCard> getRecordCardBoard();
}