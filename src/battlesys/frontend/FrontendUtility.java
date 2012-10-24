package battlesys.frontend;

import javax.swing.JOptionPane;

/**
 * Utility for front end.
 * @author Peter
 */
public class FrontendUtility {

    private FrontendUtility(){};

    /**
     * Check whether the text field had a non negative integer input, and show an error message if not.
     * @param callingComponent The component where the function is called.
     * @param textField Text field containing the text needed to check against.
     * @param fieldName The name of the field, used for showing error message.
     * @param appName The application name, used as title of the error message.
     * @return True if input is valid, false otherwise.
     */
    public static boolean checkNonnegativeTextField(java.awt.Component callingComponent, javax.swing.text.JTextComponent textField, String fieldName, String appName){
        int tp;
        try {
            tp = Integer.parseInt(textField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(callingComponent, fieldName + "必須為非負整數！", appName, JOptionPane.ERROR_MESSAGE);
            textField.setText("0");
            textField.requestFocusInWindow();
            return false;
        }
        if (tp < 0) {
            JOptionPane.showMessageDialog(callingComponent, fieldName + "必須為非負整數！", appName, JOptionPane.ERROR_MESSAGE);
            textField.setText("0");
            textField.requestFocusInWindow();
            return false;
        }
        return true;
    }

    /**
     * Check whether the text field had a positive integer input, and show an error message if not.
     * @param callingComponent The component where the function is called.
     * @param textField Text field containing the text needed to check against.
     * @param fieldName The name of the field, used for showing error message.
     * @param appName The application name, used as title of the error message.
     * @return True if input is valid, false otherwise.
     */
    public static boolean checkPositiveTextField(java.awt.Component callingComponent, javax.swing.text.JTextComponent textField, String fieldName, String appName){
        int tp;
        try {
            tp = Integer.parseInt(textField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(callingComponent, fieldName + "必須為非負整數！", appName, JOptionPane.ERROR_MESSAGE);
            textField.setText("0");
            textField.requestFocusInWindow();
            return false;
        }
        if (tp <= 0) {
            JOptionPane.showMessageDialog(callingComponent, fieldName + "必須為非負整數！", appName, JOptionPane.ERROR_MESSAGE);
            textField.setText("0");
            textField.requestFocusInWindow();
            return false;
        }
        return true;
    }

}
