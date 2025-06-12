package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Animal {

    private static final Logger logger = LoggerFactory.getLogger(Animal.class);

    void speak() {
        logger.info("Animal speaks");
    }
}

class Dog extends Animal {

    private static final Logger logger = LoggerFactory.getLogger(Dog.class);

    void bark() {
        logger.info("Dog barks");
    }
}
