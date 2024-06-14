package com.maple.checklist.domain.character.usecase;

import java.io.IOException;
import org.json.simple.parser.ParseException;

public interface UpdateCharacterUseCase {

    void updateAllCharacterInformation() throws IOException, ParseException, InterruptedException;
}
