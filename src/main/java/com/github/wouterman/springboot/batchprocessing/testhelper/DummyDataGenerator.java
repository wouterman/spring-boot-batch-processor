package com.github.wouterman.springboot.batchprocessing.testhelper;

import com.github.wouterman.springboot.batchprocessing.model.RegistrationType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class DummyDataGenerator {

  @SneakyThrows
  public Path generateDummyDataFile(int size, boolean randomize) {
    if(size < 0) throw new IllegalArgumentException("Generated dummy data size can't be < 0.");
    String registrationsToWrite = IntStream.range(0, size)
        .mapToObj(number -> number
            + " "
            + LocalDateTime.now()
            + " "
            + (randomize ?
              ThreadLocalRandom.current().nextBoolean() ?
                  LocalDate.now().plusDays(1) : LocalDate.now()
              : LocalDate.now())
            + " "
            + "name"
            + number
            + " "
            + "NL"
            + " "
            + "1234567"
            + " "
            + RegistrationType.NEW
            + System.lineSeparator())
        .collect(Collectors.joining());
    Path tempFile = Files.createTempFile("temp", ".txt");
    Files.write(tempFile, registrationsToWrite.getBytes());
    return tempFile;
  }
}
