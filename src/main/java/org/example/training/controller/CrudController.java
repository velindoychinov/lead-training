package org.example.training.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface CrudController<D, F, E> {
  @PostMapping(
    value = "",
    consumes = "application/json",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.CREATED)
  D create(@RequestBody D dto);

  @PutMapping(
    value = "/{id:\\d+}",
    consumes = "application/json",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  D update(@PathVariable int id, @RequestBody D dto);

  @DeleteMapping(
    value = "/{id:\\d+}"
  )
  @ResponseStatus(HttpStatus.OK)
  void delete(@PathVariable int id);

  @GetMapping(
    value = "/find",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  Page<D> find(@ParameterObject @ModelAttribute F filter);

  @GetMapping(
    value = "/{id:\\d+}",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  D getById(@PathVariable int id);

  E toEntity(D dto);

  D toDto(E entity);
}
