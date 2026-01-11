package controller.option;

import model.extended.RecordCard;
import model.Transaction;

import java.util.Map;

public interface TransactionParent {
    Map<Transaction, RecordCard> getRecordCardBoard();
}