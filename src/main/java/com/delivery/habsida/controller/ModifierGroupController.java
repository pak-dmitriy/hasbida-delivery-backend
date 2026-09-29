package com.delivery.habsida.controller;

import com.delivery.habsida.dto.ModifierGroupDto;
import com.delivery.habsida.dto.ModifierGroupRequest;
import com.delivery.habsida.service.ModifierGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/modifier-groups")
public class ModifierGroupController {

    private final ModifierGroupService modifierGroupService;

    public ModifierGroupController(ModifierGroupService modifierGroupService) {
        this.modifierGroupService = modifierGroupService;
    }

    @PostMapping
    public ResponseEntity<ModifierGroupDto> createModifierGroup(@PathVariable Long storeId,
                                                                @RequestBody @Valid ModifierGroupRequest request) {
        ModifierGroupDto created = modifierGroupService.createModifierGroup(storeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<ModifierGroupDto> getModifierGroups(@PathVariable Long storeId) {
        return modifierGroupService.getModifierGroups(storeId);
    }

    @GetMapping("/{groupId}")
    public ModifierGroupDto getModifierGroup(@PathVariable Long storeId,
                                             @PathVariable Long groupId) {
        return modifierGroupService.getModifierGroup(storeId, groupId);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<ModifierGroupDto> updateModifierGroup(@PathVariable Long storeId,
                                                                @PathVariable Long groupId,
                                                                @RequestBody @Valid ModifierGroupRequest request) {
        ModifierGroupDto updated = modifierGroupService.updateModifierGroup(storeId, groupId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteModifierGroup(@PathVariable Long storeId,
                                                    @PathVariable Long groupId) {
        modifierGroupService.deleteModifierGroup(storeId, groupId);
        return ResponseEntity.noContent().build();
    }

}
