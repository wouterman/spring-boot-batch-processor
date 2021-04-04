package com.github.wouterman.springboot.batchprocessing.testhelper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DummyDataGeneratorTest {

  private DummyDataGenerator generator;

  @BeforeEach
  void setup() {
    generator = new DummyDataGenerator();
  }

  @Test
  void generateDummyDataFile() throws IOException {
    Path filePath = generator.generateDummyDataFile(100, false);
    List<String> lines = Files.readAllLines(filePath);
    assertEquals(100, lines.size());
    String line = lines.get(0);
    assertTrue(line.matches("0 .* .* name0 NL 1234567 NEW"));
  }

}