package com.example.karmashopbe.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Builder
@Document(collection = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    @Indexed
    private String productName;
    private List<String> images;
    private Float price;
    private List<String> categoryId;

}
