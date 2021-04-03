package com.github.wouterman.springboot.batchprocessing.processor;

import com.github.wouterman.springboot.batchprocessing.model.ProcessingResult;
import java.nio.file.Path;

public interface RegistrationProcessor {

      ProcessingResult process(Path filePath);
}
