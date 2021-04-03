package com.github.wouterman.springboot.batchprocessing.repository;


import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.Registration.RegistrationView;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegistrationRepository extends PagingAndSortingRepository<Registration, Long> {

  boolean existsByCodeAndModificationDate(int code, LocalDate modificationDate);

  Page<RegistrationView> findAllProjectedBy(Pageable pageable);

}
