package com.github.wouterman.springboot.batchprocessing.processor;

import com.github.wouterman.springboot.batchprocessing.model.Registration;
import com.github.wouterman.springboot.batchprocessing.model.RegistrationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class RegistrationParser {

  public Registration fromString(String string) {
    String[] split = string.split(" ");
    return new Registration(null,
        Integer.parseInt(split[0]),
        LocalDateTime.parse(split[1]),
        LocalDate.parse(split[2]),
        split[3],
        split[4],
        split[5],
        RegistrationType.valueOf(split[6]));
  }

}
