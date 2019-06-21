/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class RobotJCR {

    public static void executar(String string, int delay) {
        int[] array = new int[string.length()];
        for (int x = 0; x < string.length(); x++) {
            array[x] = (int) string.charAt(x);
        }
        try {
            Robot r = new Robot();
            for (int x = 0; x < array.length; x++) {
                r.keyPress(array[x]);
                r.delay(delay);
            }
        } catch (AWTException ex) {
            Logger.getLogger(RobotJCR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void executar(int keyEvent, int delay) {
        try {
            Robot r = new Robot();
            r.keyPress(keyEvent);
            r.delay(delay);
        } catch (AWTException ex) {
            Logger.getLogger(RobotJCR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void AltTab(int delay) {
        Robot r;
        try {
            r = new Robot();
            r.keyPress(KeyEvent.VK_ALT);
            r.keyPress(KeyEvent.VK_TAB);
            r.delay(delay);
            r.keyRelease(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            Logger.getLogger(RobotJCR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
