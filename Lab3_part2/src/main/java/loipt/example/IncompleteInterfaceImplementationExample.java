package loipt.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface Shape {
    void draw();
    void resize();
}

class Square implements Shape {
    private static final Logger logger = LoggerFactory.getLogger(Square.class);

    @Override
    public void draw() {
        // Thay thế System.out.println bằng logger
        logger.info("Drawing square");
    }

    @Override
    public void resize() {
        // Cài đặt phương thức resize
        logger.info("Resizing square");
    }
}
