package com.github.wouterman.springboot.batchprocessing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "registration")
@Table(name = "registration")
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo")
  @SequenceGenerator(name = "hilo", sequenceName = "hilo", allocationSize = 100_000)
  private Long id;
  @Column(name = "code")
  private String code;
  @Column(name = "creation_timestamp")
  private LocalDateTime creationTimestamp;
  @Column(name = "modification_date")
  private LocalDate modificationDate;
  @Column(name = "name")
  private String name;
  @Column(name = "country_code")
  private String countryCode;
  @Column(name = "telephone")
  private String telephone;
  @Column(name = "type")
  private RegistrationType type;

  public interface RegistrationView {

    String getCode();

    LocalDate getModificationDate();
  }
}
