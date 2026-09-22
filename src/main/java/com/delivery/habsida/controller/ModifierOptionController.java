package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ModifierOptionDto;
import com.delivery.habsida.dto.ModifierOptionRequest;
import com.delivery.habsida.service.ModifierOptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/modifier-groups/{groupId}/options")
public class ModifierOptionController {
    private final ModifierOptionService modifierOptionService;

    public ModifierOptionController(ModifierOptionService modifierOptionService) {
        this.modifierOptionService = modifierOptionService;
    }

    @PostMapping
    public ResponseEntity<ModifierOptionDto> createModifierOption(@PathVariable Long storeId,
                                                                  @PathVariable Long groupId,
                                                                  @RequestBody @Valid ModifierOptionRequest request) {
        ModifierOptionDto created = modifierOptionService.createModifierOption(storeId, groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{optionId}")
    public ModifierOptionDto getModifierOption(@PathVariable Long storeId,
                                               @PathVariable Long groupId,
                                               @PathVariable Long optionId) {
        return modifierOptionService.getModifierOption(storeId, groupId, optionId);
    }

    @GetMapping
    public List<ModifierOptionDto> getModifierOptions(@PathVariable Long storeId,
                                                      @PathVariable Long groupId) {
        return modifierOptionService.getModifierOptions(storeId, groupId);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<ModifierOptionDto> updateModifierOption(@PathVariable Long storeId,
                                                                  @PathVariable Long groupId,
                                                                  @PathVariable Long optionId,
                                                                  @RequestBody @Valid ModifierOptionRequest request) {
        ModifierOptionDto updated = modifierOptionService.updateModifierOption(storeId, groupId, optionId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteModifierOption(@PathVariable Long storeId,
                                                     @PathVariable Long groupId,
                                                     @PathVariable Long optionId) {
        modifierOptionService.deleteModifierOption(storeId, groupId, optionId);
        return ResponseEntity.noContent().build();
    }
}
