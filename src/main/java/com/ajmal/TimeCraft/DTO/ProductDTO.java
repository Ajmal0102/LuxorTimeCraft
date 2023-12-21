package com.ajmal.TimeCraft.DTO;

import com.ajmal.TimeCraft.Entity.Category;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private int categoryId;
    private double price;
    private int quantity;
    private String imageName;
}
