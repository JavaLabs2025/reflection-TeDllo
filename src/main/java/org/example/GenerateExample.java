package org.example;


import org.example.classes.BinaryTreeNode;
import org.example.classes.Cart;
import org.example.classes.Example;
import org.example.classes.PrimitiveExample;
import org.example.classes.Product;
import org.example.classes.Rectangle;
import org.example.classes.Shape;
import org.example.classes.Triangle;
import org.example.generator.Generator;
import org.example.generator.NotGeneratableClassException;

public class GenerateExample {
    public static void main(String[] args) {
        var gen = new Generator();
        try {
            System.out.println(gen.generateValueOfType(Example.class));
            System.out.println(gen.generateValueOfType(Cart.class));
            System.out.println(gen.generateValueOfType(BinaryTreeNode.class));
            System.out.println(gen.generateValueOfType(Product.class));
            System.out.println(gen.generateValueOfType(Rectangle.class));
            System.out.println(gen.generateValueOfType(Shape.class));
            System.out.println(gen.generateValueOfType(Triangle.class));
            System.out.println(gen.generateValueOfType(PrimitiveExample.class));

            try {
                System.out.println(gen.generateValueOfType(GenerateExample.class));
            } catch (NotGeneratableClassException e) {
                System.err.println(e.getMessage());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
