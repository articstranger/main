package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditBeneficiaryCommand;
import seedu.address.logic.commands.EditBeneficiaryCommand.EditBeneficiaryDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditBeneficiaryCommandParser implements Parser<EditBeneficiaryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditBeneficiaryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditBeneficiaryCommand.MESSAGE_USAGE), pe);
        }

        EditBeneficiaryDescriptor editBeneficiaryDescriptor = new EditBeneficiaryDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editBeneficiaryDescriptor.setName(ParserUtilBeneficiary.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editBeneficiaryDescriptor.setPhone(ParserUtilBeneficiary.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editBeneficiaryDescriptor.setEmail(ParserUtilBeneficiary.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editBeneficiaryDescriptor.setAddress(ParserUtilBeneficiary.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        if (!editBeneficiaryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditBeneficiaryCommand.MESSAGE_NOT_EDITED);
        }

        return new EditBeneficiaryCommand(index, editBeneficiaryDescriptor);
    }
}
