package controller.option;

import model.RecordCard;
import model.Transaction;

import java.util.Map;

public interface TransactionParent {
    Map<Transaction, RecordCard> getRecordCardBoard();
}