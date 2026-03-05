import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BMICalculatorUI extends JFrame implements ActionListener {

    JTextField weightField, heightField;
    JLabel resultLabel;
    JRadioButton metricBtn, englishBtn;
    JButton calculateBtn, clearBtn;

    public BMICalculatorUI() {

        setTitle("BMI Calculator");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7,2,10,10));

        // Unit selection
        metricBtn = new JRadioButton("Metric (Kg / Meters)", true);
        englishBtn = new JRadioButton("English (Pounds / Inches)");

        ButtonGroup group = new ButtonGroup();
        group.add(metricBtn);
        group.add(englishBtn);

        // Text fields
        weightField = new JTextField();
        heightField = new JTextField();

        // Buttons
        calculateBtn = new JButton("Calculate BMI");
        calculateBtn.addActionListener(this);

        clearBtn = new JButton("Clear");

        // Clear button action
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                weightField.setText("");
                heightField.setText("");
                resultLabel.setText("Result: ");
                resultLabel.setForeground(Color.BLACK);
                weightField.requestFocus();
            }
        });

        // Result label
        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add components
        add(new JLabel("Select Unit"));
        add(new JLabel(""));

        add(metricBtn);
        add(englishBtn);

        add(new JLabel("Weight"));
        add(weightField);

        add(new JLabel("Height"));
        add(heightField);

        add(calculateBtn);
        add(clearBtn);

        add(resultLabel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        try {

            double weight = Double.parseDouble(weightField.getText());
            double height = Double.parseDouble(heightField.getText());

            if(weight <= 0 || height <= 0){
                JOptionPane.showMessageDialog(this,
                        "Weight and Height must be positive numbers!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double bmi;

            if(metricBtn.isSelected())
                bmi = weight / (height * height);
            else
                bmi = (weight * 703) / (height * height);

            String category;

            if(bmi < 18.5){
                category = "Underweight";
                resultLabel.setForeground(Color.BLUE);
            }
            else if(bmi <= 24.9){
                category = "Normal";
                resultLabel.setForeground(Color.GREEN);
            }
            else if(bmi <= 29.9){
                category = "Overweight";
                resultLabel.setForeground(Color.ORANGE);
            }
            else{
                category = "Obese";
                resultLabel.setForeground(Color.RED);
            }

            resultLabel.setText("BMI: " + String.format("%.2f", bmi) + " (" + category + ")");

        }
        catch(NumberFormatException ex){

            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BMICalculatorUI();
    }
}