package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.*;
import seedu.address.model.beneficiary.Beneficiary;
import seedu.address.model.person.Person;
import seedu.address.model.volunteer.Volunteer;
import seedu.address.model.project.Project;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method"
                    + " should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public void addVolunteer(Volunteer volunteer) {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public void addProject(Project project) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method"
                    + " should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method"
                    + " should not be called.");
        }

        @Override
        public boolean hasVolunteer(Volunteer volunteer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method"
                    + " should not be called.");
        }

        @Override
        public void deleteProject(Project target) {

        }
        @Override
        public void deleteBeneficiary(Beneficiary target) {

        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setBeneficiary(Beneficiary target, Beneficiary editedBeneficiary) {

        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Project> getFilteredProjectList() { return null; }

        @Override
        public ObservableList<Beneficiary> getFilteredBeneficiaryList() {
            return null;
        }

        @Override
        public ObservableList<seedu.address.model.volunteer.Volunteer> getFilteredVolunteerList() {
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateFilteredProjectList(Predicate<Project> predicate) {

        }
        @Override
        public void updateFilteredBeneficiaryList(Predicate<Beneficiary> predicate) {

        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method"
                    + " should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Person> selectedPersonProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Beneficiary> selectedBeneficiaryProperty() {
            return null;
        }

        @Override
        public Beneficiary getSelectedBeneficiary() {
            return null;
        }

        @Override
        public Person getSelectedPerson() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedPerson(Person person) {
            throw new AssertionError("This method "
                    + "should not be called.");
        }

        @Override
        public void setSelectedBeneficiary(Beneficiary beneficiary) {

        }

        @Override
        public void deleteVolunteer(seedu.address.model.volunteer.Volunteer target) {

        }

        @Override
        public void setVolunteer(seedu.address.model.volunteer.Volunteer target, seedu.address.model.volunteer.Volunteer editedVolunteer) {

        }

        @Override
        public ReadOnlyProperty<seedu.address.model.volunteer.Volunteer> selectedVolunteerProperty() {
            return null;
        }

        @Override
        public seedu.address.model.volunteer.Volunteer getSelectedVolunteer() {
            return null;
        }

        @Override
        public void setSelectedVolunteer(seedu.address.model.volunteer.Volunteer volunteer) {

        }

        @Override
        public void updateFilteredVolunteerList(Predicate<seedu.address.model.volunteer.Volunteer> predicate) {

        }

        @Override
        public int checkAge(MapObject map, Volunteer currentVol) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int checkRace(MapObject map, Volunteer currentVol) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int checkMedical(MapObject map, Volunteer currentVol) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void mapAllVolunteer(MapObject map) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addBeneficiary(Beneficiary beneficiary) {

        }

        @Override
        public boolean hasBeneficiary(Beneficiary beneficiary) {
            return false;
        }


    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
