package com.github.wouterman.springboot.batchprocessing.repository;


import com.github.wouterman.springboot.batchprocessing.model.Registration;
import java.time.LocalDate;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegistrationRepository extends PagingAndSortingRepository<Registration, Long> {

  boolean existsByCodeAndModificationDate(int code, LocalDate modificationDate);

}
