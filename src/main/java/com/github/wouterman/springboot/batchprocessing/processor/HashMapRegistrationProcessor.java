package com.github.wouterman.springboot.batchprocessing.processor;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.repository.RegistrationRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component("hashMapRegistrationProcessor")
public class HashMapRegistrationProcessor implements RegistrationProcessor {

  private final RegistrationParser parser;
  private final RegistrationRepository repository;

  public HashMapRegistrationProcessor(
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
    List<String> lines = Files.readAllLines(filePath);
    Map<Integer, Registration> registrations = lines
        .stream()
        .map(parser::fromString)
        .collect(Collectors.toMap(Registration::getCode, registration -> registration));
    Pageable pageable = PageRequest.of(0, 100_000);
    Page<Registration> allRegistrations = repository.findAll(pageable);
    allRegistrations.stream().forEach(persisted -> {
      Registration duplicate = registrations.get(persisted.getCode());
      if(duplicate != null
          && persisted.getModificationDate().equals(duplicate.getModificationDate())) {
          registrations.remove(persisted.getCode());
          result.incrementDuplicates();
        }
      });
    registrations.values().forEach(registration -> result.incrementAdded());
    repository.saveAll(registrations.values());
    return result;
  }
}
