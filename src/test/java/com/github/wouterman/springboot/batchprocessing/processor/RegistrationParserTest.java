package com.github.wouterman.springboot.batchprocessing.processor;

import static org.junit.jupiter.api.Assertions.*;

import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.RegistrationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationParserTest {

  private RegistrationParser parser;

  @BeforeEach
  void setUp() {
    parser = new RegistrationParser();
  }

  @Test
  void fromString() {
    String timestamp = "2021-04-04T14:00:27.045031200";
    String date = "2021-04-04";
    String line = "0 "+ timestamp + " " + date + " name0 NL 1234567 NEW";
    Registration actual = parser.fromString(line);
    assertAll(
        () -> assertEquals("0", actual.getCode()),
        () -> assertEquals(LocalDateTime.parse(timestamp), actual.getCreationTimestamp()),
        () -> assertEquals(LocalDate.parse(date), actual.getModificationDate()),
        () -> assertEquals("name0", actual.getName()),
        () -> assertEquals("NL", actual.getCountryCode()),
        () -> assertEquals("1234567", actual.getTelephone()),
        () -> assertEquals(RegistrationType.NEW, actual.getType())
    );
  }
}