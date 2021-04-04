package com.github.wouterman.springboot.batchprocessing.repository;


import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.Registration.RegistrationView;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationRepository extends PagingAndSortingRepository<Registration, Long> {

  boolean existsByCodeAndModificationDate(String code, LocalDate modificationDate);

  @Transactional(readOnly = true)
  Page<RegistrationView> findAllProjectedBy(Pageable pageRequest);
}
