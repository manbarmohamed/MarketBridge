package com.api.marketbridge.favorite.mapper;

import com.api.marketbridge.favorite.dto.FavoriteRequest;
import com.api.marketbridge.favorite.dto.FavoriteResponse;
import com.api.marketbridge.favorite.entity.Favorite;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.user.entity.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProduct")
    Favorite toEntity(FavoriteRequest favoriteRequest);

    @Mapping(target = "product", source = "product")
    FavoriteResponse toDto(Favorite favorite);

    @Named("mapBuyer")
    default Buyer mapBuyer(Long id) {
        if (id == null) return null;
        Buyer buyer = new Buyer();
        buyer.setId(id);
        return buyer;
    }

    @Named("mapProduct")
    default Product mapProduct(Long id) {
        if (id == null) return null;
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
