package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.BeneficiaryCommandTestUtil.showBeneficiaryAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalBeneficiaries.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.beneficiary.Beneficiary;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteBeneficiaryCommand}.
 */
public class DeleteBeneficiaryCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Beneficiary beneficiaryToDelete = model.getFilteredBeneficiaryList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteBeneficiaryCommand deleteBeneficiaryCommand = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteBeneficiaryCommand.MESSAGE_DELETE_BENEFICIARY_SUCCESS, beneficiaryToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteBeneficiary(beneficiaryToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteBeneficiaryCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredBeneficiaryList().size() + 1);
        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_BENEFICIARY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showBeneficiaryAtIndex(model, INDEX_FIRST_PERSON);

        Beneficiary beneficiaryToDelete = model.getFilteredBeneficiaryList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteBeneficiaryCommand.MESSAGE_DELETE_BENEFICIARY_SUCCESS, beneficiaryToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteBeneficiary(beneficiaryToDelete);
        expectedModel.commitAddressBook();
        showNoBeneficiary(expectedModel);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showBeneficiaryAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getBeneficiaryList().size());

        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_BENEFICIARY_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Beneficiary beneficiaryToDelete = model.getFilteredBeneficiaryList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteBeneficiary(beneficiaryToDelete);
        expectedModel.commitAddressBook();

        // delete -> first beneficiary deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered beneficiary list to show all beneficiarys
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first beneficiary deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredBeneficiaryList().size() + 1);
        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(outOfBoundIndex);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory, Messages.MESSAGE_INVALID_BENEFICIARY_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Beneficiary} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted beneficiary in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the beneficiary object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameBeneficiaryDeleted() throws Exception {
        DeleteBeneficiaryCommand deleteCommand = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        showBeneficiaryAtIndex(model, INDEX_SECOND_PERSON);
        Beneficiary beneficiaryToDelete = model.getFilteredBeneficiaryList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.deleteBeneficiary(beneficiaryToDelete);
        expectedModel.commitAddressBook();

        // delete -> deletes second beneficiary in unfiltered beneficiary list / first beneficiary in filtered beneficiary list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered beneficiary list to show all beneficiarys
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> deletes same second beneficiary in unfiltered beneficiary list
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteBeneficiaryCommand deleteFirstCommand = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);
        DeleteBeneficiaryCommand deleteSecondCommand = new DeleteBeneficiaryCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteBeneficiaryCommand deleteFirstCommandCopy = new DeleteBeneficiaryCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different beneficiary -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void execute_invalidDelete_haveAttachedProjeccts() throws Exception {
        
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoBeneficiary(Model model) {
        model.updateFilteredBeneficiaryList(p -> false);

        assertTrue(model.getFilteredBeneficiaryList().isEmpty());
    }
}
