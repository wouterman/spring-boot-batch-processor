package com.github.wouterman.springboot.batchprocessing.model;

import lombok.Getter;

public class ProcessingResult {

  @Getter
  private int added;
  @Getter
  private int duplicates;

  public void incrementAdded() {
    added++;
  }

  public void incrementDuplicates() {
    duplicates++;
  }
}
