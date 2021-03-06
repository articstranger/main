package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.beneficiary.SummaryBeneficiaryCommand;
import seedu.address.logic.commands.beneficiary.SummaryBeneficiaryCommand.SummarisedBeneficiary;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.beneficiary.BeneficiaryListPanel;
import seedu.address.ui.project.ProjectListPanel;
import seedu.address.ui.volunteer.VolunteerListPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private ValidatePassword validatePassword;

    // Independent Ui parts residing in this Ui container
    private ProjectListPanel projectListPanel;
    private VolunteerListPanel volunteerListPanel;
    private BeneficiaryListPanel beneficiaryListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane projectListPanelPlaceholder;

    @FXML
    private StackPane beneficiaryListPanelPlaceholder;

    @FXML
    private StackPane volunteerListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }
    //@@swalahlah
    /**
     * Returns true if password is correct, indicating User has logged in.
     */
    boolean show(boolean []user) {
        primaryStage.show();
        ValidatePassword.display(user);
        if (user[0]) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        beneficiaryListPanel = new BeneficiaryListPanel(logic.getFilteredBeneficiaryList(),
            logic.selectedBeneficiaryProperty(), logic::setSelectedBeneficiary);
        beneficiaryListPanelPlaceholder.getChildren().add(beneficiaryListPanel.getRoot());

        volunteerListPanel = new VolunteerListPanel(logic.getFilteredVolunteerList(),
            logic.selectedVolunteerProperty(), logic::setSelectedVolunteer);
        volunteerListPanelPlaceholder.getChildren().add(volunteerListPanel.getRoot());

        projectListPanel = new ProjectListPanel(logic.getFilteredProjectList(),
            logic.selectedProjectProperty(), logic::setSelectedProject);
        projectListPanelPlaceholder.getChildren().add(projectListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath(), logic.getAddressBook());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand, logic.getHistory());
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    //@@author ndhuu

    /**
     * open summary table window.
     */
    @FXML
    public void handleBeneficiarySummary() {
        Stage stage = new Stage();
        TableView<SummarisedBeneficiary> table = new TableView<SummarisedBeneficiary>();
        final ObservableList<SummarisedBeneficiary> data = SummaryBeneficiaryCommand.getSummarisedBeneficiaries(logic);
        Scene scene = SummaryBeneficiaryCommand.drawSummaryTable(stage, table, data);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
            (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public VolunteerListPanel getVolunteerListPanel() {
        return volunteerListPanel;

    }

    public BeneficiaryListPanel getBeneficiaryListPanel() {
        return beneficiaryListPanel;
    }

    public ProjectListPanel getProjectListPanel() {
        return projectListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isShowBeneficiarySummary()) {
                handleBeneficiarySummary();
                commandResult.resetShowBeneficiarySummary();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
