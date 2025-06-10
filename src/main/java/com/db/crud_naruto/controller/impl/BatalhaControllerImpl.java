package com.db.crud_naruto.controller.impl;

import com.db.crud_naruto.controller.BatalhaController;
import com.db.crud_naruto.service.BatalhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/batalhas")
public class BatalhaControllerImpl implements BatalhaController {

    private final BatalhaService batalhaService;

    @Override
    @GetMapping("/{ninja1}/{ninja2}")
    public ResponseEntity<String> iniciarBatalha(@PathVariable Long ninja1, @PathVariable Long ninja2) {
        return ResponseEntity.ok(batalhaService.iniciarBatalha(ninja1, ninja2));
    }
}
