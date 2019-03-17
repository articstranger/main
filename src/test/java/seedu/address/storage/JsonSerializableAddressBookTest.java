package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.TypicalVolunteers;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_VOLUNTEERS_FILE = TEST_DATA_FOLDER.resolve("typicalVolunteersAddressBook.json");
    private static final Path INVALID_VOLUNTEER_FILE = TEST_DATA_FOLDER.resolve("invalidVolunteerAddressBook.json");
    private static final Path DUPLICATE_VOLUNTEER_FILE = TEST_DATA_FOLDER.resolve("duplicateVolunteerAddressBook.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalVolunteersFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_VOLUNTEERS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalVolunteersAddressBook = TypicalVolunteers.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalVolunteersAddressBook);
    }

    @Test
    public void toModelType_invalidVolunteerFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_VOLUNTEER_FILE,
                JsonSerializableAddressBook.class).get();
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateVolunteers_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_VOLUNTEER_FILE,
                JsonSerializableAddressBook.class).get();
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(JsonSerializableAddressBook.MESSAGE_DUPLICATE_VOLUNTEER);
        dataFromFile.toModelType();
    }

}
