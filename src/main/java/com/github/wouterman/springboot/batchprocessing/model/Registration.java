package com.github.wouterman.springboot.batchprocessing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "registration")
@Table(name = "registration")
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo")
  @GenericGenerator(name = "hilo",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @Parameter(name = "sequence_name", value = "hilo_sequence"),
          @Parameter(name = "initial_value", value = "1"),
          @Parameter(name = "increment_size", value = "10"),
          @Parameter(name = "optimizer", value = "hilo")
      })
  @EqualsAndHashCode.Include
  private Long id;
  @Column(name = "code")
  private int code;
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
}
