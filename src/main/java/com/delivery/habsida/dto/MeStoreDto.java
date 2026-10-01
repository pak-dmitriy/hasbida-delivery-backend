package com.delivery.habsida.dto;

import com.delivery.habsida.entity.Store;

public record MeStoreDto (
        Long id,
        String name
){
   public static MeStoreDto from(Store store) {
       return new MeStoreDto(
               store.getId(),
               store.getName()
       );
   }
}
