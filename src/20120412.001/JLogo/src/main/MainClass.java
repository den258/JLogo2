package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jlogo.JLogo;

import utils.Utils;

import component.Command;
import component.Point;

class MyPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<Point> objPoints = new ArrayList<Point>();

	/**
	 *
	 */
	@Override
	public void paint(Graphics g) {

		g.setColor(new Color(255, 255, 255));
		g.fillRect(5, 5, 772, 387);

		g.setColor(new Color(200, 200, 200));
		g.drawRect(5, 5, 772, 384);

		g.setColor(new Color(0, 0, 0));

		Point objBeforePoint = null;

		for (Point objPoint : objPoints) {
			objPoint.intX += 386;
			objPoint.intY += 193;
		}

		g.drawRect(- 5, - 5, 10, 10);

		for (Point objPoint : objPoints) {

			if (objBeforePoint != null) {
				g.drawLine(
						objBeforePoint.intX,
						objBeforePoint.intY,
						objPoint.intX,
						objPoint.intY);
			}

			objBeforePoint = objPoint;

			System.out.println("objBeforePoint.intX = " + Utils.getString(objBeforePoint.intX - 5));
			System.out.println("objBeforePoint.intY = " + Utils.getString(objBeforePoint.intY - 5));

			g.drawRect(
					objBeforePoint.intX - 5,
					objBeforePoint.intY - 5,
					10,
					10);

		}


	}

	public void setPoints(List<Point> objArgPoints) {
		this.objPoints = objArgPoints;
	}

}

class MyExecProgramButtonMouseListener implements MouseListener {

	private MyPanel objMyPanel = null;
	private JTextArea objTextArea = null;

	public MyExecProgramButtonMouseListener(
			MyPanel objMyPanel, JTextArea objTextArea) {
		this.objMyPanel = objMyPanel;
		this.objTextArea = objTextArea;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		JLogo objLogo = new JLogo();
		List<Command> objCommands = new ArrayList<Command>();

		try {

			objCommands =
					objLogo.parse(
							objLogo.validate(
									objTextArea.getText()));

			objLogo.objPoints.clear();
			objLogo.objPoints.add(new Point(0, 0));

			objMyPanel.setPoints(objLogo.execute(objCommands));
			objMyPanel.repaint();

		} catch (Exception objExp) {
			objExp.printStackTrace();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

}

public class MainClass extends JFrame {

	/**
	 * @see
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	MyPanel objMyPanel = null;
	JButton objButton = null;
	JTextArea objTextArea = null;
	JScrollPane objScrollPane = null;

	/**
	 * @see コンストラクタ
	 */
	public MainClass() {

		this.setLayout(null);

		objMyPanel = new MyPanel();
		objMyPanel.setBounds(5, 5, 780, 390);
		this.add(objMyPanel);

		objButton = new JButton("プログラムを実行");
		objButton.setBounds(633, 400, 150, 45);
		this.add(objButton);

		objTextArea = new JTextArea();

		objTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

		String strInitialProgram = new String();
		strInitialProgram += "set intVar = 0;\r\n";
		strInitialProgram += "loop 35;\r\n";
		strInitialProgram += "  forward 10;\r\n";
		strInitialProgram += "  right intVar;\r\n";
		strInitialProgram += "  set intVar = intVar + 5;\r\n";
		strInitialProgram += "end;\r\n";

		objTextArea.setText(strInitialProgram);

		objScrollPane =
				new JScrollPane(objTextArea);
		objScrollPane.setBounds(5, 450, 780, 215);

		this.add(objScrollPane);

		objButton.addMouseListener(
				new MyExecProgramButtonMouseListener(
						objMyPanel, objTextArea));

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		(new Expression("")).testCase20120411001();
//		(new Expression("")).testCase20120412001();
//		(new JLogo()).testCase20120405001();
//		(new JLogo()).testCase20120412001();
//		(new JLogo()).testCase20120412002();
//		(new JLogo()).testCase20120412003();

		/**
		 * 画面起動
		 */
		MainClass objForm = new MainClass();
		objForm.setSize(800, 700);
		objForm.setVisible(true);

	}

}
