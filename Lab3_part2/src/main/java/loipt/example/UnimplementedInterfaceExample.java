package loipt.example;

import java.util.logging.Logger;

interface Drawable {
    void draw();
}

class Circle implements Drawable {
    // Cài đặt phương thức draw() trong lớp Circle
    @Override
    public void draw() {
        // Thay thế System.out.println bằng logger
        Logger logger = Logger.getLogger(Circle.class.getName());
        logger.info("Drawing a circle");
    }
}

