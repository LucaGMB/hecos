package com.hecos.entity.nutrition;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NutritionRecord extends IntervalHealthRecord {

    private String name;
    private Integer mealType;
    private Double energyKcal;
    private Double energyFromFatKcal;
    private Double biotinGrams;
    private Double caffeineGrams;
    private Double calciumGrams;
    private Double chlorideGrams;
    private Double cholesterolGrams;
    private Double chromiumGrams;
    private Double copperGrams;
    private Double dietaryFiberGrams;
    private Double folateGrams;
    private Double folicAcidGrams;
    private Double iodineGrams;
    private Double ironGrams;
    private Double magnesiumGrams;
    private Double manganeseGrams;
    private Double molybdenumGrams;
    private Double monounsaturatedFatGrams;
    private Double niacinGrams;
    private Double pantothenicAcidGrams;
    private Double phosphorusGrams;
    private Double polyunsaturatedFatGrams;
    private Double potassiumGrams;
    private Double proteinGrams;
    private Double riboflavinGrams;
    private Double saturatedFatGrams;
    private Double seleniumGrams;
    private Double sodiumGrams;
    private Double sugarGrams;
    private Double thiaminGrams;
    private Double totalCarbohydrateGrams;
    private Double totalFatGrams;
    private Double transFatGrams;
    private Double unsaturatedFatGrams;
    private Double vitaminAGrams;
    private Double vitaminB6Grams;
    private Double vitaminB12Grams;
    private Double vitaminCGrams;
    private Double vitaminDGrams;
    private Double vitaminEGrams;
    private Double vitaminKGrams;
    private Double zincGrams;
}
