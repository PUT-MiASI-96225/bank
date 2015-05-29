package pl.mefiu.bank;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.pdfbox.exceptions.COSVisitorException;
import pl.mefiu.bank.AccountPeriodReportGenerator.AccountPeriodReportGeneratorContext;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public final class CustomerController implements Initializable, IChangeableView {

    @FXML
    private Button exitBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button removeAccountBtn;

    @FXML
    private Button removeCustomerBtn;

    @FXML
    private Button addCustomerEmployerBtn;

    @FXML
    private Button createAccountBtn;

    @FXML
    private Button depositReportBtn;

    @FXML
    private Button withdrawReportBtn;

    @FXML
    private Button periodReportBtn;

    @FXML
    private Button fullReportBtn;

    @FXML
    private Button transferBetweenAccountsBtn;

    @FXML
    private Button transferBetweenBanksBtn;

    @FXML
    private Button depositBtn;

    @FXML
    private Button withdrawBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setValue(LocalDate.now());
        accountDatePicker.setValue(LocalDate.now());
        reportStartDatePicker.setValue(LocalDate.now());
        reportEndDatePicker.setValue(LocalDate.now());
        accObservableList = FXCollections.observableArrayList();
        transactionObservableList = FXCollections.observableArrayList();
        cusEmpObservableList = FXCollections.observableArrayList();
        custEmpTableView.setItems(cusEmpObservableList);
        accTableView.setItems(accObservableList);
        transTableView.setItems(transactionObservableList);
        otherAccountComboBox.setOnMouseClicked(event -> {
            otherAccountComboBox.getItems().clear();
            ((ElixirBankMediator) getiBankMediator()).getBanks().forEach((s, iBank) -> {
                iBank.fetchAllAccounts().stream().forEach(acc -> {
                    String str = acc.getIban() + ";" + acc.getAccountNumber();
                    otherAccountComboBox.getItems().add(str);
                });
            });
        });
        accTableView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Integer> change) {
                getTransactions();
            }

        });
    }

    public String getText() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        if(stage != null) {
            this.stage = stage;
        } else {
            throw new IllegalArgumentException("stage cannot be null!");
        }
    }

    public IBankMediator getiBankMediator() {
        return iBankMediator;
    }

    public void setiBankMediator(IBankMediator iBankMediator) {
        if(iBankMediator != null) {
            this.iBankMediator = iBankMediator;
        } else {
            throw new IllegalArgumentException("iBankMediator cannot be null!");
        }
    }

    public IBank getActiveIBank() {
        return activeIBank;
    }

    public void setActiveIBank(IBank activeIBank) {
        if(activeIBank != null) {
            this.activeIBank = activeIBank;
        } else {
            throw new IllegalArgumentException("activeIBank cannot be null!");
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if(customer != null) {
            this.customer = customer;
        } else {
            throw new IllegalArgumentException("customer cannot be null!");
        }
        getAccounts();
        getCustomerEmployers();
        getTransactions();
    }

    public ObservableList<Account> getAccObservableList() {
        return accObservableList;
    }

    public void setAccObservableList(ObservableList<Account> accObservableList) {
        this.accObservableList = accObservableList;
    }

    public ObservableList<CustomerEmployer> getCusEmpObservableList() {
        return cusEmpObservableList;
    }

    public void setCusEmpObservableList(ObservableList<CustomerEmployer> cusEmpObservableList) {
        this.cusEmpObservableList = cusEmpObservableList;
    }

    public ObservableList<Transaction> getTransactionObservableList() {
        return transactionObservableList;
    }

    public void setTransactionObservableList(ObservableList<Transaction> transactionObservableList) {
        this.transactionObservableList = transactionObservableList;
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        if(getStage() != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root = fxmlLoader.load();
            Initializable controller = fxmlLoader.getController();
            ((IChangeableView) controller).setStage(getStage());
            Scene scene = new Scene(root, 1400, 800);
            getStage().setScene(scene);
            getStage().show();
        } else {
            throw new IllegalArgumentException("stage cannot be null!");
        }
    }

    public void handleExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void getAccounts() {
        accObservableList.clear();
        customer.getAccounts().stream().forEach(accObservableList::add);
        accTableView.setItems(accObservableList);
    }

    private void getCustomerEmployers() {
        cusEmpObservableList.clear();
        total = 0.0;
        customer.getCustomerEmployers().stream().forEach(customerEmployer -> {
            total += customerEmployer.getSalary();
            cusEmpObservableList.add(customerEmployer);
        });
        String str = "";
        str += customer.getFirstName() + ", ";
        str += customer.getSecondName() + ", ";
        str += customer.getPin() + ", ";
        str += customer.getPassword() + ", ";
        str += total.toString();
        setText(str);
        custEmpTableView.setItems(cusEmpObservableList);
    }

    private void getTransactions() {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            transactionObservableList.clear();
            account.getTransactions().stream().forEach(transactionObservableList::add);
            accTableView.setItems(accObservableList);
        }
    }

    public void handleMakeWithdrawReport(ActionEvent actionEvent) throws COSVisitorException, ParseException, IOException {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            IBank ib = getActiveIBank();
            String path = ib.getReportGeneratorPath();
            ib.setAccountVisitor(new AccountWithdrawReportGenerator(path));
            ib.generateReport(account);
        }
    }

    public void handleMakeDepositReport(ActionEvent actionEvent) throws COSVisitorException, ParseException, IOException {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            IBank ib = getActiveIBank();
            String path = ib.getReportGeneratorPath();
            ib.setAccountVisitor(new AccountDepositReportGenerator(path));
            ib.generateReport(account);
        }
    }

    public void handleMakeFullReport(ActionEvent actionEvent) throws COSVisitorException, ParseException, IOException {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            IBank ib = getActiveIBank();
            String path = ib.getReportGeneratorPath();
            ib.setAccountVisitor(new AccountFullReportGenerator(path));
            ib.generateReport(account);
        }
    }

    public void handleMakePeriodReport(ActionEvent actionEvent) throws COSVisitorException, ParseException, IOException {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            IBank ib = getActiveIBank();
            String path = ib.getReportGeneratorPath();
            LocalDate startDate = reportStartDatePicker.getValue();
            LocalDate endDate = reportEndDatePicker.getValue();
            Instant startInstant = Instant.from(startDate.atStartOfDay(ZoneId.systemDefault()));
            Instant endInstant = Instant.from(endDate.atStartOfDay(ZoneId.systemDefault()));
            Date start = Date.from(startInstant);
            Date end = Date.from(endInstant);
            AccountPeriodReportGeneratorContext accountPeriodReportGeneratorContext =
                    new AccountPeriodReportGeneratorContext(start, end, path);
            ib.setAccountVisitor(new AccountPeriodReportGenerator(accountPeriodReportGeneratorContext));
            ib.generateReport(account);
        }
    }

    public void handleMakeWithdraw(ActionEvent actionEvent) {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account == null) {
            return;
        }
        Double balance = account.getBalance();
        if(balance == 0.0) {
            Double amount = Double.parseDouble(amountTextField.getText());
            Account debitAccount = getActiveIBank().createDebitAccount(account);
            getActiveIBank().makeWithdraw(debitAccount, amount, false, onSuccess, onFail, onProcessing);
        } else if(balance > 0.0) {
            Double amount = Double.parseDouble(amountTextField.getText());
            getActiveIBank().makeWithdraw(account, amount, false, onSuccess, onFail, onProcessing);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occured.");
            alert.setContentText("Cannot make debit twice!");
            alert.showAndWait();
        }
    }

    public void handleMakeDeposit(ActionEvent actionEvent) {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account == null) {
            return;
        }
        Double amount = Double.parseDouble(amountTextField.getText());
        getActiveIBank().makeDeposit(account, amount, false, onSuccess, onFail, onProcessing);
    }

    public void handleMakeTransferBetweenAccounts(ActionEvent actionEvent) {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account == null) {
            return;
        }
        String otherAccount = (String) otherAccountComboBox.getSelectionModel().getSelectedItem();
        if(otherAccount == null) {
            return;
        }
        String bank = otherAccount.split(";")[0];
        if(!bank.equals(activeIBank.getClass().getSimpleName())) {
            return;
        }
        String otherAccountNumber = otherAccount.split(";")[1];
        Double amount = Double.parseDouble(amountTextField.getText());
        getActiveIBank().makeTransferBetweenAccounts(onSuccess, onFail, onProcessing, amount, otherAccountNumber,
                account, false);
    }

    public void handleMakeTransferBetweenBanks(ActionEvent actionEvent) {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account == null) {
            return;
        }
        String otherAccount = (String) otherAccountComboBox.getValue();
        if(otherAccount == null) {
            return;
        }
        String bank = otherAccount.split(";")[0];
        if(bank.equals(activeIBank.getClass().getSimpleName())) {
            return;
        }
        String otherAccountNumber = otherAccount.split(";")[1];
        Double amount = Double.parseDouble(amountTextField.getText());
        getActiveIBank().makeTransferBetweenBanks(onSuccess, onFail, onProcessing, amount, otherAccountNumber, bank,
                account, false);
    }

    public void handleAddAccount(ActionEvent actionEvent) {
        LocalDate localDate = accountDatePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        getActiveIBank().createAccount(date, Double.parseDouble(balanceTextField.getText()), getCustomer());
        customer = getActiveIBank().customerByPin(getCustomer().getPin());
        getAccounts();
    }

    public void handleRemoveAccount(ActionEvent actionEvent) {
        Account account = (Account) accTableView.getSelectionModel().getSelectedItem();
        if(account != null) {
            getActiveIBank().deleteAccount(account);
            customer = getActiveIBank().customerByPin(getCustomer().getPin());
            getAccounts();
        }
    }

    public void handleAddCustomerEmployer(ActionEvent actionEvent) {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        getActiveIBank().createCustomerEmployer(date, Double.parseDouble(salaryTextField.getText()),
                cityTextField.getText(), postalCodeTextField.getText(), streetTextField.getText(),
                apartmentNumberTextField.getText(), stateTextField.getText(), emailTextField.getText(),
                phoneNumberTextField.getText(), nameTextField.getText(), customer);
        customer = getActiveIBank().customerByPin(getCustomer().getPin());
        getCustomerEmployers();
    }

    public void handleRemoveCustomerEmployer(ActionEvent actionEvent) {
        CustomerEmployer customerEmployer = (CustomerEmployer) custEmpTableView.getSelectionModel().getSelectedItem();
        if(customerEmployer != null) {
            getActiveIBank().deleteCustomerEmployer(customerEmployer);
            customer = getActiveIBank().customerByPin(getCustomer().getPin());
            getCustomerEmployers();
        }
    }

    private void enableAll() {
        exitBtn.setDisable(false);
        logoutBtn.setDisable(false);
        removeAccountBtn.setDisable(false);
        removeCustomerBtn.setDisable(false);
        addCustomerEmployerBtn.setDisable(false);
        createAccountBtn.setDisable(false);
        depositReportBtn.setDisable(false);
        withdrawReportBtn.setDisable(false);
        periodReportBtn.setDisable(false);
        fullReportBtn.setDisable(false);
        transferBetweenAccountsBtn.setDisable(false);
        transferBetweenBanksBtn.setDisable(false);
        depositBtn.setDisable(false);
        withdrawBtn.setDisable(false);
    }

    private Double total;

    private Customer customer;

    private IBank activeIBank;

    private Stage stage;

    private IBankMediator iBankMediator;

    private ObservableList<Account> accObservableList;

    private ObservableList<CustomerEmployer> cusEmpObservableList;

    private ObservableList<Transaction> transactionObservableList;

    private SimpleStringProperty text = new SimpleStringProperty();;

    @FXML
    private TableView accTableView;

    @FXML
    private TableView transTableView;

    @FXML
    private TableView custEmpTableView;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private TextField apartmentNumberTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private DatePicker accountDatePicker;

    @FXML
    private TextField balanceTextField;

    @FXML
    private ComboBox otherAccountComboBox;

    @FXML
    private TextField amountTextField;

    @FXML
    private DatePicker reportStartDatePicker;

    @FXML
    private DatePicker reportEndDatePicker;

    EventHandler<WorkerStateEvent> onSuccess = new EventHandler<WorkerStateEvent>() {

        @Override
        public void handle(WorkerStateEvent event) {
            customer = getActiveIBank().customerByPin(getCustomer().getPin());
            getAccounts();
            enableAll();
        }

    };

    EventHandler<WorkerStateEvent> onFail = new EventHandler<WorkerStateEvent>() {

        @Override
        public void handle(WorkerStateEvent event) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occured.");
            alert.setContentText("Error while making transaction");
            alert.showAndWait();
            customer = getActiveIBank().customerByPin(getCustomer().getPin());
            getAccounts();
            enableAll();
        }

    };

    EventHandler<WorkerStateEvent> onProcessing = new EventHandler<WorkerStateEvent>() {

        @Override
        public void handle(WorkerStateEvent event) {
            exitBtn.setDisable(true);
            logoutBtn.setDisable(true);
            removeAccountBtn.setDisable(true);
            removeCustomerBtn.setDisable(true);
            addCustomerEmployerBtn.setDisable(true);
            createAccountBtn.setDisable(true);
            depositReportBtn.setDisable(true);
            withdrawReportBtn.setDisable(true);
            periodReportBtn.setDisable(true);
            fullReportBtn.setDisable(true);
            transferBetweenAccountsBtn.setDisable(true);
            transferBetweenBanksBtn.setDisable(true);
            depositBtn.setDisable(true);
            withdrawBtn.setDisable(true);
        }

    };

}