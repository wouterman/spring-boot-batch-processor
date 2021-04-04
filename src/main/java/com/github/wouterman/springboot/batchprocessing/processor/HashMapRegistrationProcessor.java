package com.github.wouterman.springboot.batchprocessing.processor;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.Registration.RegistrationView;
import com.github.wouterman.springboot.batchprocessing.repository.RegistrationRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
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
    Map<String, Registration> registrations = lines
        .stream()
        .map(parser::fromString)
        .collect(Collectors.toMap(Registration::getCode, registration -> registration));

    Pageable pageRequest = PageRequest.of(0, 1_000_000, Sort.by(Direction.ASC, "code"));
    Page<RegistrationView> allRegistrations = repository.findAllProjectedBy(pageRequest);
    while (!allRegistrations.isEmpty()) {
      pageRequest = pageRequest.next();
      allRegistrations.forEach(persisted -> {
        Registration duplicate = registrations.get(persisted.getCode());
        if (duplicate != null
            && persisted.getModificationDate().equals(duplicate.getModificationDate())) {
          registrations.remove(duplicate.getCode());
          result.incrementDuplicates();
        }
      });
      allRegistrations = repository.findAllProjectedBy(pageRequest);
    }
    registrations.values().forEach(registration -> result.incrementAdded());
    log.info("Starting saveAll operation.");
    repository.saveAll(registrations.values());
    return result;
  }
}
