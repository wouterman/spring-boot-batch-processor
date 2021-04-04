package com.github.wouterman.springboot.batchprocessing.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import com.github.wouterman.springboot.batchprocessing.repository.RegistrationRepository;
import com.github.wouterman.springboot.batchprocessing.testhelper.DummyDataGenerator;
import java.nio.file.Path;
import org.h2.store.fs.FilePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DataJpaTest(showSql = false)
class HashMapRegistrationProcessorTest {

  @Autowired
  private RegistrationRepository repository;

  private HashMapRegistrationProcessor processor;
  private DummyDataGenerator generator;

  @BeforeEach
  void setUp() {
    RegistrationParser parser = new RegistrationParser();
    processor = new HashMapRegistrationProcessor(parser, repository);
    generator = new DummyDataGenerator();
  }

  @Test
  void process_shouldProcessAllRecordsInFile() {
    Path filePath = generator.generateDummyDataFile(1000, false);
    ProcessingResult result = processor.process(filePath);
    assertEquals(1000, result.getAdded());
    assertEquals(0, result.getDuplicates());
  }

  @Test
  void process_shouldHandleDuplicates() {
    Path filePath = generator.generateDummyDataFile(1000, false);
    ProcessingResult result = processor.process(filePath);
    assertEquals(1000, result.getAdded());
    assertEquals(0, result.getDuplicates());

    ProcessingResult second = processor.process(filePath);
    assertEquals(0, second.getAdded());
    assertEquals(1000, second.getDuplicates());
  }

  @Test
  void process_shouldHandleMillionEntries() {
    Path filePath = generator.generateDummyDataFile(1_000_000, false);
    ProcessingResult result = processor.process(filePath);
    assertEquals(1_000_000, result.getAdded());
    assertEquals(0, result.getDuplicates());

    Path duplicateFilePath = generator.generateDummyDataFile(1_000_000, false);
    ProcessingResult second = processor.process(duplicateFilePath);
    assertEquals(0, second.getAdded());
    assertEquals(1_000_000, second.getDuplicates());
  }

}