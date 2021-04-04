package com.github.wouterman.springboot.batchprocessing;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import com.github.wouterman.springboot.batchprocessing.processor.HashMapRegistrationProcessor;
import com.github.wouterman.springboot.batchprocessing.processor.RegistrationProcessor;
import com.github.wouterman.springboot.batchprocessing.processor.RegularRegistrationProcessor;
import com.github.wouterman.springboot.batchprocessing.repository.RegistrationRepository;
import com.github.wouterman.springboot.batchprocessing.testhelper.DummyDataGenerator;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class BatchProcessingConfiguration {

  private static final int REGISTRATIONS_TO_GENERATE = 1_000_000;

  @Profile("default")
  @Bean
  public CommandLineRunner useRegularRegistrationProcessor(
      DummyDataGenerator dummyDataGenerator,
      RegularRegistrationProcessor processor) {
    return args -> {
      log.info("Processing registrations using REGULAR processor.");
      log.info("Starting first run WITHOUT duplicates.");
      doRun(dummyDataGenerator, processor, false);
      log.info("Starting second run WITH duplicates.");
      doRun(dummyDataGenerator, processor, true);
    };
  }

  @Profile("hashmap")
  @Bean
  public CommandLineRunner useHashMapRegistrationProcessor(
      DummyDataGenerator dummyDataGenerator,
      HashMapRegistrationProcessor processor,
      RegistrationRepository repository) {
    return args -> {
      log.info("Processing registrations using HASHMAP processor.");
      log.info("Starting first run WITHOUT duplicates.");
      doRun(dummyDataGenerator, processor, false);
      log.info("Starting second run WITH duplicates.");
      doRun(dummyDataGenerator, processor, true);
      log.info("Total registrations: {}", repository.count());
    };
  }

  private void doRun(DummyDataGenerator dummyDataGenerator,
      RegistrationProcessor processor,
      boolean randomize) {
    Path filePath = dummyDataGenerator
        .generateDummyDataFile(REGISTRATIONS_TO_GENERATE, randomize);
    log.info("Generated file. Starting processing.");
    long startTime = System.nanoTime();
    ProcessingResult result = processor.process(filePath);
    long endTime = System.nanoTime();
    log.info("Results: ");
    log.info("Processing took {} ms.", (endTime - startTime) / 1_000_000);
    log.info("Added {} new registrations.", result.getAdded());
    log.info("Counted {} duplicates.", result.getDuplicates());
  }


}
