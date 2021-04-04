package com.github.wouterman.springboot.batchprocessing.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.Registration.RegistrationView;
import com.github.wouterman.springboot.batchprocessing.model.RegistrationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest(showSql = false)
class RegistrationRepositoryTest {

  @Autowired
  private RegistrationRepository repository;
  private List<Registration> inserted;

  @BeforeEach
  public void setup() {
    inserted = IntStream.range(0, 10)
        .mapToObj(value ->
            new Registration(
              null,
              String.valueOf(value),
                LocalDateTime.now(),
                LocalDate.now(),
                "name" + value,
                "NL",
                "123456",
                RegistrationType.NEW
            ))
        .collect(Collectors.toList());
    repository.saveAll(inserted);
  }

  @Test
  void existsByCodeAndModificationDate_shouldReturnTrue_whenEntryExists() {
    Registration registration = inserted.get(0);
    boolean result = repository.existsByCodeAndModificationDate(registration.getCode(), registration.getModificationDate());
    assertTrue(result);
  }

  @Test
  void existsByCodeAndModificationDate_shouldReturnFalse_whenEntryDoesNotExist() {
    boolean result = repository.existsByCodeAndModificationDate("DOES_NOT_EXIST", LocalDate.now());
    assertFalse(result);
  }

  @Test
  void findAllProjectedBy_shouldFindAllRegistrations() {
    Pageable page = PageRequest.of(0, 100);
    Page<RegistrationView> allProjectedBy = repository.findAllProjectedBy(page);
    assertEquals(inserted.size(), allProjectedBy.getTotalElements());
  }
}