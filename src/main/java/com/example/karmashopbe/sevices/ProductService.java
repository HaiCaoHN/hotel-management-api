package com.example.karmashopbe.sevices;

import com.example.karmashopbe.models.Category;
import com.example.karmashopbe.models.Product;
import com.example.karmashopbe.models.Property;
import com.example.karmashopbe.models.SubCategory;
import com.example.karmashopbe.repositories.CategoryRepository;
import com.example.karmashopbe.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Transactional
    public void initDb() {
        List<String> values = List.of("64GB", "128GB","256GB", "512GB");
        List<Property> props = List.of(new Property("Storage",values));
        SubCategory subCate = SubCategory.builder()
                .name("IOS").properties(props).build();
        Category category = Category.builder().name("Mobiles").properties(props).subCategories(List.of(subCate)).build();

        Product product = Product.builder()
                .productName("Redmi Note7")
                .price(1200.0f)
                .images(null)
                .categoryId(List.of("1","2"))
                .build();

        productRepository.save(product);
        categoryRepository.save(category);

    }
}
