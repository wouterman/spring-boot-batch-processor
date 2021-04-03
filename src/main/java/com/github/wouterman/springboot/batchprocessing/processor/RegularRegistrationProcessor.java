package com.github.wouterman.springboot.batchprocessing.processor;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.repository.RegistrationRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("regularRegistrationProcessor")
public class RegularRegistrationProcessor implements RegistrationProcessor {

  private final RegistrationParser parser;
  private final RegistrationRepository repository;

  public RegularRegistrationProcessor(
      RegistrationParser parser,
      RegistrationRepository repository) {
    this.parser = parser;
    this.repository = repository;
  }

  @SneakyThrows
  @Transactional
  @Override
  public ProcessingResult process(Path filePath) {
    ProcessingResult result = new ProcessingResult();
    List<Registration> toPersist = new ArrayList<>();
    List<String> lines = Files.readAllLines(filePath);
    long count = 0;
    for(String line : lines) {
      Registration registration = parser.fromString(line);
      boolean duplicate = repository.existsByCodeAndModificationDate(registration.getCode(), registration.getModificationDate());
      if(!duplicate) {
        toPersist.add(registration);
        result.incrementAdded();
      } else {
        result.incrementDuplicates();
      }
      if (count % 100_000 == 0) {
        log.info("Processed {} registrations.", count);
      }
      count++;
    }
    repository.saveAll(toPersist);
    return result;
  }
}
