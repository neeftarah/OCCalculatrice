package net.ddns.jmoreau.calculatrice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * TP OpenClassrooms - Apprenez à programmer en Java
 * Une calculatrice
 */
public class Calculatrice implements KeyListener {
	private static Double value = 0d;
	private static JLabel resultLabel = new JLabel();
	private static boolean newCalculation = true;
	private static String currentOperator = null;
	private JFrame fenetre = new JFrame();
	
	// Patern de validation d'un double pour éviter la saisie de deux virgules
	final static String DOUBLE_PATTERN = "[0-9]+(\\.){0,1}[0-9]*";

	/**
	 * La méthode main sert de point d'entrée pour l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Calculatrice();
	}
		
	/**
	 * Le constructeur initialise la calculatrice, ses boutons, etc.
	 */
	public Calculatrice() {
		// Définition de la fenêtre principale
		fenetre.setTitle("Calculatrice");
		fenetre.setSize(new Dimension(250, 300));
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setLocationRelativeTo(null);
		
		// Définition du panel principal
		JPanel mainContainer = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(5);
		layout.setVgap(5);
		mainContainer.setLayout(layout);
		fenetre.setContentPane(mainContainer);
		
		// Définition des éléments
		// Zone de texte de la calculatrice
		resultLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		resultLabel.setForeground(Color.black);
		resultLabel.setHorizontalAlignment(JLabel.RIGHT);
		resultLabel.setText("0");
		resultLabel.setBorder(LineBorder.createGrayLineBorder());
		mainContainer.add(resultLabel, BorderLayout.NORTH);
		
		// Zone des nombres + Egale
		JPanel numberContainer = new JPanel();
		GridLayout numberLayout = new GridLayout(4, 3);
		numberLayout.setHgap(5);
		numberLayout.setVgap(5);
		numberContainer.setLayout(numberLayout);

		numberContainer.add(new NumberButton("1"));
		numberContainer.add(new NumberButton("2"));
		numberContainer.add(new NumberButton("3"));
		numberContainer.add(new NumberButton("4"));
		numberContainer.add(new NumberButton("5"));
		numberContainer.add(new NumberButton("6"));
		numberContainer.add(new NumberButton("7"));
		numberContainer.add(new NumberButton("8"));
		numberContainer.add(new NumberButton("9"));
		numberContainer.add(new NumberButton("."));
		numberContainer.add(new NumberButton("0"));
		numberContainer.add(new EgalButton());
		
		mainContainer.add(numberContainer, BorderLayout.CENTER);

		// Zone des opérateurs
		JPanel operatorContainer = new JPanel();
		operatorContainer.setPreferredSize(new Dimension(50, 300));
		GridLayout operatorLayout = new GridLayout(5, 1);
		operatorLayout.setHgap(5);
		operatorLayout.setVgap(5);
		operatorContainer.setLayout(operatorLayout);
		
		operatorContainer.add(new OperatorButton("C"));
		operatorContainer.add(new OperatorButton("÷"));
		operatorContainer.add(new OperatorButton("×"));
		operatorContainer.add(new OperatorButton("–"));
		operatorContainer.add(new OperatorButton("+"));

		mainContainer.add(operatorContainer, BorderLayout.EAST);
		
		fenetre.setVisible(true);
		fenetre.addKeyListener(this);
		fenetre.requestFocus();
	}
	
	/**
	 * Effectue les calculs en fonction de l'opérateur sélectionné
	 */
	protected static void calculate() {
		if (newCalculation || currentOperator == null) {
			value = Double.valueOf(resultLabel.getText());
		} else {
			switch (currentOperator) {
				case "+":
					value += Double.valueOf(resultLabel.getText());
					break;
				case "–":
					value -= Double.valueOf(resultLabel.getText());
					break;
				case "×":
					value *= Double.valueOf(resultLabel.getText());
					break;
				case "÷":
					value /= Double.valueOf(resultLabel.getText());
					break;
			}
		}

		resultLabel.setText((value == Math.floor(value)) ? String.valueOf(Math.round(value)) : String.valueOf(value));
	}
	
	protected void addNumber(String number) {
		String newNumber = (resultLabel.getText() == "0" || newCalculation) ? number : resultLabel.getText() + number;
		if (".".equals(newNumber)) newNumber = "0.";
		
		if (Pattern.matches(DOUBLE_PATTERN, newNumber)) {
			resultLabel.setText(newNumber);
		}
		
		newCalculation = false;
	}
	
	protected void proccessOperator(String operator) {
		if ("C".equals(operator)) {
			value = 0d;
			currentOperator = null;
			resultLabel.setText("0");
		} else {
			calculate();
			currentOperator = operator;
		}
		
		newCalculation = true;
	}
	
	protected void calculateResult() {
		calculate();
		currentOperator = null;
		newCalculation = true;
	}
	
	/**
	 * Classe Interne pour facilité la création des boutons d'opérateur et de leur action
	 */
	class OperatorButton extends JButton implements ActionListener  {
		/** Generated serialization ID */
		private static final long serialVersionUID = 5096319221574288454L;

		public OperatorButton(String value) {
			super(value);
			this.addActionListener(this);
			
			if ("C".equals(value)) {
				this.setForeground(Color.RED);
			}
		}
		
		public void actionPerformed(ActionEvent e){
			String pressedOperator = ((JButton)e.getSource()).getText();
			proccessOperator(pressedOperator);
		}
	}

	/**
	 * Classe Interne pour facilité la création du bouton égal et de ses actions
	 */
	class EgalButton extends JButton implements ActionListener  {
		/** Generated serialization ID */
		private static final long serialVersionUID = 5096319221574288454L;

		public EgalButton() {
			super("=");
			this.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e){
			calculateResult();
		}
	}

	/**
	 * Classe Interne pour facilité la création des boutons de nombres et de leur action
	 */
	class NumberButton extends JButton implements ActionListener  {
		/** Generated serialization ID */
		private static final long serialVersionUID = 3606707804065554874L;
		
		public NumberButton(String value) {
			super(value);
			this.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e){
      		String number = ((JButton)e.getSource()).getText();
      		addNumber(number);
		}
	}

	
	@Override
	public void keyPressed(KeyEvent e) {}
	

	@Override
	public void keyReleased(KeyEvent e) {
		char keyPressed = e.getKeyChar();

		// Si le caractère est un nombre ou un point, on l'ajoute à la chaîne de calcul
		if (Character.isDigit(keyPressed) || keyPressed == '.') {
			addNumber(String.valueOf(keyPressed));
			
		// Si c'est un opérateur
		// Touches C, SUPP ou DEL
		} else if (keyPressed == 'C' || e.getKeyCode() == 127 || e.getKeyCode() == 8) {
			proccessOperator("C");
			
		// Touches *
		} else if (e.getKeyCode() == 106) {
			proccessOperator("×");

		// Touches /
		} else if (e.getKeyCode() == 111) {
			proccessOperator("÷");
			
		// Touches +
		} else if (keyPressed == '+' || e.getKeyCode() == 107) {
			proccessOperator("+");
			
		// Touches -
		} else if (keyPressed == '-' || e.getKeyCode() == 109) {
			proccessOperator("–");

		// Touches = ou ENTRER => résultat
		} else if(e.getKeyCode() == 10 || e.getKeyCode() == 61) {
			calculateResult();
			
		// Touches ECHAP => Quitte l'appli
		} else if(e.getKeyCode() == 27) {
			fenetre.dispatchEvent(new WindowEvent(fenetre, WindowEvent.WINDOW_CLOSING));
		}
	}
	

	@Override
	public void keyTyped(KeyEvent e) {}
}
