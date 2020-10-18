package cr.ac.itcr.UI;

import java.io.IOException;
import java.net.BindException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        try{
            Window window = new Window();
        } catch (BindException e) {
            Main.log.debug("Server activo");
            Main.log.warn(e.getMessage(), e);
        }
    }
}
