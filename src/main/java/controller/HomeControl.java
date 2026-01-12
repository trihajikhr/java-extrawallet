package controller;

import dataflow.DataManager;
import helper.Converter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import model.*;
import model.enums.TransactionType;
import model.extended.CategoryBlock;
import model.extended.RecordCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CurrencyApiClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HomeControl implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(HomeControl.class);
    private static final String BASE_CURRENCY = "IDR";

    private ArrayList<Transaction> dataTransaction;

    @FXML private VBox latestTransactionPanel;
    @FXML private VBox latestCategoriesPanel;

    @FXML AreaChart<String, Number> grafikArea;

    @FXML private Label incomeLabel;
    @FXML private Label expenseLabel;
    @FXML private Label balanceLabel;
    @FXML private Label persenIncome;
    @FXML private Label persenExpense;
    @FXML private Label persenBalance;

    // data class record card category
    private ArrayList<CategoryBlock> filteredKategori = new ArrayList<>();

    // data bigdecimal
    private BigDecimal lastIncome60 = BigDecimal.ZERO;
    private BigDecimal lastExpense60 = BigDecimal.ZERO;
    private BigDecimal lastTotalIncome = BigDecimal.ZERO;
    private BigDecimal lastTotalExpense = BigDecimal.ZERO;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        fetchData();
        generateChart();
        generateLatestTransactionPanel();
        setLabelAndCountLastMonth();
        setLabelAndAccumulateAmount();
        setMostUsedCategories();
    }

    // [0] >=== INITIALISASI & DATA FETCHING
    private void fetchData() {
        dataTransaction = DataManager.getInstance().getDataTransaksi();
    }

    // [1] >=== LABEL SET
    private BigDecimal normalizeAmount(Transaction trans) {
        BigDecimal amount = trans.getAmount();
        String currency = trans.getAccount().getCurrencyType().getCode();
        if (currency.equalsIgnoreCase(BASE_CURRENCY)) {
            return amount;
        }
        BigDecimal converted = CurrencyApiClient.getInstance().convert(amount, currency, BASE_CURRENCY);
        return converted;
    }
    private void setLabelAndCountLastMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(30);
        LocalDate previousDate = today.minusDays(60);

        List<Transaction> previousDays = dataTransaction.stream()
                .filter(t ->
                        !t.getDate().isBefore(previousDate) &&
                                !t.getDate().isAfter(startDate)
                )
                .toList();

        List<Transaction> last30Days = dataTransaction.stream()
                .filter(t ->
                        !t.getDate().isBefore(startDate) &&
                                !t.getDate().isAfter(today)
                )
                .toList();

        for(Transaction trans : previousDays) {
            if(trans.getTransactionType() == TransactionType.INCOME) {
                lastIncome60 = lastIncome60.add(normalizeAmount(trans));
            } else if(trans.getTransactionType() == TransactionType.EXPANSE) {
                lastExpense60 = lastExpense60.add(normalizeAmount(trans));
            }
        }

        for(Transaction trans : last30Days) {
            if(trans.getTransactionType() == TransactionType.INCOME) {
                lastTotalIncome = lastTotalIncome.add(normalizeAmount(trans));
            } else if(trans.getTransactionType() == TransactionType.EXPANSE) {
                lastTotalExpense = lastTotalExpense.add(normalizeAmount(trans));
            }
        }

        // label all set
        setLabel(incomeLabel, lastTotalIncome);
        setLabel(expenseLabel, lastTotalExpense);

        // persentasi set
        setPercentageBadge(persenIncome, lastTotalIncome, lastIncome60);
        setPercentageBadge(persenExpense, lastTotalExpense, lastExpense60);
    }
    private void setLabelAndAccumulateAmount() {
        BigDecimal allIncome = BigDecimal.ZERO;
        BigDecimal allExpense = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalBalanceBefore = BigDecimal.ZERO;

        for(Transaction trans : dataTransaction) {
            if(trans.getTransactionType() == TransactionType.INCOME) {
                allIncome = allIncome.add(normalizeAmount(trans));
            } else if(trans.getTransactionType() == TransactionType.EXPANSE) {
                allExpense = allExpense.add(normalizeAmount(trans));
            }
        }

        totalBalance = allIncome.subtract(allExpense);
        totalBalanceBefore = totalBalance.subtract(lastTotalIncome.subtract(lastTotalExpense));

        setLabel(balanceLabel, totalBalance);
        setPercentageBadge(persenBalance, totalBalance, totalBalanceBefore);
    }
    private void setPercentageBadge(Label badge, BigDecimal current, BigDecimal previous) {

        // base style (badge shape)
        String baseStyle = """
        -fx-padding: 3 8 3 8;
        -fx-background-radius: 999;
        -fx-font-size: 11;
        -fx-font-weight: bold;
    """;

        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            if (current.compareTo(BigDecimal.ZERO) == 0) {
                badge.setText("0%");
                badge.setStyle(baseStyle +
                        "-fx-text-fill: #6A7282;" +
                        "-fx-background-color: rgba(156,163,175,0.25);"
                );
            } else {
                badge.setText("New");
                badge.setStyle(baseStyle +
                        "-fx-text-fill: #01AA71;" +
                        "-fx-background-color: rgba(1,170,113,0.15);"
                );
            }
            return;
        }

        BigDecimal percent = current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 0, RoundingMode.HALF_UP); // 0 decimal


        int sign = percent.signum();

        if (sign > 0) {
            badge.setText("↑ +" + percent + "%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #01AA71;" +
                    "-fx-background-color: rgba(1,170,113,0.15);"
            );
        } else if (sign < 0) {
            badge.setText("↓ -" + percent.abs() + "%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #F92222;" +
                    "-fx-background-color: rgba(249,34,34,0.15);"
            );
        } else {
            badge.setText("0%");
            badge.setStyle(baseStyle +
                    "-fx-text-fill: #6A7282;" +
                    "-fx-background-color: rgba(156,163,175,0.25);"
            );
        }
    }
    private void setLabel(Label label, BigDecimal value) {
        label.setText("IDR " + Converter.numberFormatter(value.toPlainString()));
    }

    // [2] >=== CHART VIEW SETUP
    private void generateChart() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29);

        Map<LocalDate, BigDecimal> incomePerDay = new LinkedHashMap<>();
        Map<LocalDate, BigDecimal> expensePerDay = new LinkedHashMap<>();

        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            incomePerDay.put(date, BigDecimal.ZERO);
            expensePerDay.put(date, BigDecimal.ZERO);
        }

        for (Transaction t : dataTransaction) {
            LocalDate date = t.getDate();

            if (!date.isBefore(startDate) && !date.isAfter(today)) {
                BigDecimal amount = normalizeAmount(t);

                if (t.getTransactionType() == TransactionType.INCOME) {
                    incomePerDay.put(
                            date,
                            incomePerDay.get(date).add(amount)
                    );
                } else {
                    expensePerDay.put(
                            date,
                            expensePerDay.get(date).add(amount)
                    );
                }
            }
        }

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        for (LocalDate date : incomePerDay.keySet()) {
            incomeSeries.getData().add(
                    new XYChart.Data<>(
                            date.format(formatter),
                            incomePerDay.get(date)
                    )
            );

            expenseSeries.getData().add(
                    new XYChart.Data<>(
                            date.format(formatter),
                            expensePerDay.get(date)
                    )
            );
        }

        // grafikLine.getXAxis().setLabel("Date");
        grafikArea.setCreateSymbols(false);
        grafikArea.getYAxis().setLabel("Amount");
        grafikArea.getData().clear();
        grafikArea.getData().addAll(incomeSeries, expenseSeries);
    }

    // [3] >=== PANEL 8 CARD KATEGORI
    private void setMostUsedCategories() {
        mostTransactionCategories();
        createCardCategories();
    }
    private void mostTransactionCategories() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(30);

        HashMap<Category, Integer> kategoriUsed = new HashMap<>();
        HashMap<Category, BigDecimal> kategoriAmount = new HashMap<>();

        for(Category ktgr : DataManager.getInstance().getDataKategori()) {
            kategoriUsed.put(ktgr,0);
        }

        for(Transaction trans : dataTransaction) {
            if(!trans.getDate().isBefore(startDate) && !trans.getDate().isAfter(today)) {
                int temp = kategoriUsed.get(trans.getCategory()) + 1;
                kategoriUsed.put(trans.getCategory(), temp);
            }
        }

        // sorting dan ambil beberapa terbesar
        int dataLimit = 8;

        List<Map.Entry<Category, Integer>> sortedKategori =
                kategoriUsed.entrySet()
                        .stream()
                        .sorted(Map.Entry.<Category, Integer>comparingByValue().reversed())
                        .toList();

        List<Map.Entry<Category, Integer>> topKategori =
                sortedKategori.stream()
                        .limit(dataLimit)
                        .toList();

        for(int i=0; i<dataLimit; i++) {
            BigDecimal sum = BigDecimal.ZERO;

            for(Transaction trans : dataTransaction) {
                if(trans.getCategory().equals(topKategori.get(i).getKey())) {
                    sum = sum.add(normalizeAmount(trans));
                }
            }

            kategoriAmount.put(topKategori.get(i).getKey(), sum);
        }

        for (Map.Entry<Category, Integer> entry : topKategori) {
            Category category = entry.getKey();
            if(entry.getValue() == 0) continue; // skip jika 0 penggunaan!
            filteredKategori.add(
                    new CategoryBlock(
                            category,
                            entry.getValue(),
                            kategoriAmount.get(category)
                    )
            );
        }
    }
    private void createCardCategories() {
        latestCategoriesPanel.getChildren().clear();

        for(CategoryBlock ktgr : filteredKategori) {
            HBox cardWrapper = new HBox();
            cardWrapper.setPrefHeight(60);
            HBox.setHgrow(cardWrapper, Priority.ALWAYS);
            cardWrapper.setSpacing(10);
            cardWrapper.setAlignment(Pos.CENTER);
            cardWrapper.getStyleClass().add("record-card-categories");

            StackPane iconStack = createIconStackNode(ktgr.getCategory());
            VBox labelWrap = createLabelNode(ktgr);
            HBox amountLabel = createLabelAmount(ktgr);

            cardWrapper.getChildren().addAll(iconStack, labelWrap, amountLabel);
            latestCategoriesPanel.getChildren().add(cardWrapper);
        }
    }
    private StackPane createIconStackNode(Category ktgr) {
        Circle bgCircle = new Circle(20, ktgr.getColor());
        ImageView kategoriIcon = new ImageView(ktgr.getIcon());
        kategoriIcon.setFitWidth(24);
        kategoriIcon.setFitHeight(24);

        Circle clip = new Circle(12, 12, 12);
        kategoriIcon.setClip(clip);

        StackPane result = new StackPane(bgCircle, kategoriIcon);
        return result;
    }
    private VBox createLabelNode(CategoryBlock ktgr) {
        Label namaKategori = new Label(ktgr.getCategory().getName());
        Label counter = new Label(ktgr.getTotalUsed() + " transactions recorded");

        namaKategori.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            """
        );

        counter.setStyle("""
            -fx-text-fill: #9CA3AF;
            -fx-font-size: 12px;
        """);

        VBox labelWrap = new VBox(namaKategori, counter);
        labelWrap.setPrefWidth(Region.USE_COMPUTED_SIZE);
        labelWrap.setPrefHeight(Region.USE_COMPUTED_SIZE);
        labelWrap.setAlignment(Pos.CENTER_LEFT);
        return labelWrap;
    }
    private HBox createLabelAmount(CategoryBlock ktgr) {
        String stringForm = ktgr.getTotalAmount().toPlainString();
        String result = Converter.numberFormatter(stringForm);

        Label amountLabel = new Label("IDR " + result);
        amountLabel.setStyle("""
            -fx-text-fill: #000000;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
        """);

        HBox labelWrap = new HBox(amountLabel);
        HBox.setHgrow(labelWrap, Priority.ALWAYS);
        labelWrap.setAlignment(Pos.CENTER_RIGHT);
        labelWrap.setPrefHeight(50);

        return labelWrap;
    }

    // [4] >=== PANEL 15 TRANSAKSI TERAKHIR
    private RecordCard createRecordCard(Transaction trans) {
        RecordCard recordCard = new RecordCard(trans);

        // edit record card
        recordCard.getCardWrapper().getChildren().remove(0); // hapus node checklist!
        recordCard.getCardWrapper().setStyle(null);
        recordCard.getCardWrapper().getStyleClass().clear();
        recordCard.getCardWrapper().getStyleClass().add("record-card-custom");

        return recordCard;
    }
    private void generateLatestTransactionPanel(){
        log.info("data transaksi terbaru berhasil digenerate!");
        int batasData = 10;
        List<Transaction> latest10 =
                dataTransaction.stream()
                        .sorted(
                                Comparator
                                        .comparing(Transaction::getDate).reversed()
                                        .thenComparing(Transaction::getId, Comparator.reverseOrder())
                        )
                        .limit(batasData)
                        .toList();

        for (Transaction trans: latest10) {
            latestTransactionPanel.getChildren().add(createRecordCard(trans).getCardWrapper());
        }
    }
}