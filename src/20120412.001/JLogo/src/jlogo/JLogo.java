package jlogo;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

import component.Command;
import component.Control;
import component.Expression;
import jlogo.JLogo;
import component.Point;
import component.Variable;
import constants.Constant;
import exceptions.LogoParseException;
import exceptions.NotHaveEqualExpressionException;
import exceptions.TestFailureException;
import exceptions.UnDeclaredVariableException;

public class JLogo {

	private final String sLINE_END = ";";

	public Point objCurrentPos = new Point();
	public List<Point> objPoints = new ArrayList<Point>();

	private Integer intDegree = 0;
	private Integer intDistance = 0;

	private List<String> objKeywords = new ArrayList<String>();
	private List<Command> objCommands = new ArrayList<Command>();
	private List<Variable> objVariables = new ArrayList<Variable>();

	public JLogo() {

		this.objKeywords.add(Constant.STR_KEYWORD_SET);
		this.objKeywords.add(Constant.STR_KEYWORD_FORWARD);
		this.objKeywords.add(Constant.STR_KEYWORD_BACK);
		this.objKeywords.add(Constant.STR_KEYWORD_RIGHT);
		this.objKeywords.add(Constant.STR_KEYWORD_LEFT);
		this.objKeywords.add(Constant.STR_KEYWORD_LOOP);
		this.objKeywords.add(Constant.STR_KEYWORD_END);

	}

	public List<String> validate(String strArgProgram) throws Exception {

		List<String> objSentences = new ArrayList<String>();

		for (String strSentence : strArgProgram.split(";")) {
			if (strSentence.trim().length() != 0) {
				objSentences.add(strSentence.trim() + ";");
			}
		}

		/**
		 * 行が ";" セミコロンで終わっているかどうか？
		 */
		for (String strSentence : objSentences) {

			if (strSentence.endsWith(sLINE_END) == false) {
				throw new Exception();
			}

		}

		/**
		 * 行が ";" のみかどうか？
		 */
		for (String strSentence : objSentences) {

			if (strSentence.equals(sLINE_END)) {
				throw new Exception();
			}

		}

		/**
		 * 一行に複数のコマンドが含まれていないか？
		 */
		for (String strSentence : objSentences) {

			Integer intMatchCount = 0;

			for (String strKeyword : this.objKeywords) {

				if (strSentence.indexOf(strKeyword) != -1) {
					intMatchCount++;
				}
			}

			if (intMatchCount > 2) {
				throw new Exception();
			}

		}

		/**
		 * コマンドの後ろに値(数値)が指定されているか？
		 * loop の内側の行数のカウント(二重ループ非対応)
		 * loop の展開(未完成)
		 */
//		this.objCommands =
//				expLoop(
//						getLineNumberInLoop(
//								getCommandList(
//										objSentences)));

		return objSentences;

	}

	/***
	 * 単純な Command の配列を作る。
	 * @param objArgSentences
	 * @return
	 * @throws LogoParseException
	 * @throws NotHaveEqualExpressionException
	 */
	private List<Command> getCommands001(List<String> objArgSentences)
			throws LogoParseException, NotHaveEqualExpressionException {

		List<Command> objCommands = new ArrayList<Command>();

		for (String strSentence : objArgSentences) {

			Command objCommand = null;

			if (strSentence.startsWith(Constant.STR_KEYWORD_SET)) {

				String strCommand = null;
				String strExpression = null;

				try {

					strCommand =
							strSentence.substring(
									0,
									strSentence.indexOf(" "));

					strExpression =
							strSentence.substring(
									strSentence.indexOf(" ") + 1,
									strSentence.indexOf(sLINE_END));

				} catch (Exception objExp) {
					throw new LogoParseException();
				}

				Expression objExpression = new Expression(strExpression);

				objCommand =
						new Command(
								strCommand,
								objExpression);

			}

			if (strSentence.startsWith(Constant.STR_KEYWORD_FORWARD) ||
					strSentence.startsWith(Constant.STR_KEYWORD_BACK) ||
					strSentence.startsWith(Constant.STR_KEYWORD_RIGHT) ||
					strSentence.startsWith(Constant.STR_KEYWORD_LEFT) ||
					strSentence.startsWith(Constant.STR_KEYWORD_LOOP) ) {

				String strCommand = null;
				String strExpression = null;

				try {

					strCommand =
							strSentence.substring(
									0,
									strSentence.indexOf(" "));

					strExpression =
							strSentence.substring(
									strSentence.indexOf(" ") + 1,
									strSentence.indexOf(sLINE_END));

				} catch (Exception objExp) {
					throw new LogoParseException();
				}

				Expression objExpression = new Expression(strExpression);

				objCommand =
						new Command(
								strCommand,
								objExpression);

			}

			if (strSentence.startsWith(Constant.STR_KEYWORD_END)) {

				String strCommand = null;

				try {

					strCommand =
							strSentence.substring(
									0,
									strSentence.indexOf(sLINE_END));

				} catch (Exception objExp) {
					throw new LogoParseException();
				}

				objCommand =
						new Command(
								strCommand,
								null);

			}

			objCommands.add(objCommand);

		}

		return objCommands;

	}

	/**
	 * もう少し複雑な Command の配列を作る。二重ループには、非対応。
	 * @param objArgCommands
	 * @return
	 * @throws LogoParseException
	 */
	private List<Command> getCommands002(List<Command> objArgCommands) {

		List<Command> objReturnCommands = new ArrayList<Command>();

		Integer intIndex = 0;

		for (Command objCommand : objArgCommands) {

			if (objCommand.strCommand.equals(Constant.STR_KEYWORD_FORWARD) ||
					objCommand.strCommand.equals(Constant.STR_KEYWORD_BACK) ||
					objCommand.strCommand.equals(Constant.STR_KEYWORD_RIGHT) ||
					objCommand.strCommand.equals(Constant.STR_KEYWORD_LEFT) ||
					objCommand.strCommand.equals(Constant.STR_KEYWORD_SET) ) {

				objReturnCommands.add(objCommand);

			}

			if (objCommand.strCommand.equals(Constant.STR_KEYWORD_LOOP)) {

				Integer intSubIndex = intIndex;

				Control objControl =
						new Control(
								objCommand.strCommand,
								objCommand.objExpression);

				while (intSubIndex < objArgCommands.size()) {

					intSubIndex++;

					Command objSubCommand = objArgCommands.get(intSubIndex);

					if (objSubCommand.strCommand.equals(Constant.STR_KEYWORD_END)) {
						break;
					}

					objControl.objCommandList.add(objSubCommand);

				}

				objReturnCommands.add(objControl);

			}

			intIndex++;

		}

		return objReturnCommands;

	}

	/**
	 * 不要なコマンド列を削除する。
	 * @param objArgCommands
	 * @return
	 * @throws LogoParseException
	 */
	private List<Command> getCommands003(List<Command> objArgCommands) throws LogoParseException {

		Integer intLoopCommandPosition = 0;
		Boolean blnHaveLoop = false;

		for (Command objCommand : objArgCommands) {

			if (objCommand.strCommand.equals(Constant.STR_KEYWORD_LOOP)) {
				blnHaveLoop = true;
				break;
			}

			intLoopCommandPosition++;

		}

		Integer intEndOfLoopPosition = 0;

		try {

			if (blnHaveLoop == true) {
				intEndOfLoopPosition =
						intLoopCommandPosition +
						((Control)objArgCommands.get(intLoopCommandPosition)).objCommandList.size();
			}

		} catch (Exception exp) {
			throw new LogoParseException();
		}

		List<Command> objReturnCommands = new ArrayList<Command>();

		Integer intIndex = 0;

		while(intIndex < objArgCommands.size()) {

			if (intIndex <= intLoopCommandPosition) {
				objReturnCommands.add(objArgCommands.get(intIndex));
			}

			if (intLoopCommandPosition < intEndOfLoopPosition) {

				if (intEndOfLoopPosition < intIndex) {
					objReturnCommands.add(objArgCommands.get(intIndex));
				}

			}

			intIndex++;

		}

		return objReturnCommands;

	}

	/***
	 * 文字列(プログラム)を解析し、構造体を返す。
	 * @param strArgProgram
	 * @return
	 * @throws LogoParseException
	 * @throws NotHaveEqualExpressionException
	 */
	public List<Command> parse(List<String> objArgSentences)
			throws LogoParseException, NotHaveEqualExpressionException {

		List<Command> objCommands = null;

		objCommands = getCommands001(objArgSentences);
		objCommands = getCommands002(objCommands);
		objCommands = getCommands003(objCommands);

		return objCommands;

	}

	public List<Point> execute(List<Command> objArgCommands)
			throws UnDeclaredVariableException {

		List<Point> objPoints = new ArrayList<Point>();

		for (Command objCommand : objArgCommands) {

			Boolean blnAddPoint = false;

			if(objCommand.strCommand.equals("set")) {

				Variable objVariableTmp =
						Utils.getVariable(
								this.objVariables,
								objCommand.objExpression.strTargetVariableLabel);

				if (objVariableTmp == null) {

					this.objVariables.add(
							objCommand.objExpression.evaluate(
									this.objVariables));

				} else {

					objVariableTmp.intValue =
							objCommand.objExpression.evaluate(
									this.objVariables).intValue;

				}

			}

			if(objCommand.strCommand.equals("forward")) {

				Variable objVariable =
					objCommand.objExpression.evaluate(
								this.objVariables);

				intDistance = objVariable.intValue;

				blnAddPoint = true;

			}

			if(objCommand.strCommand.equals("back")) {

				Variable objVariable =
						objCommand.objExpression.evaluate(
									this.objVariables);

				intDistance = objVariable.intValue * -1;

				blnAddPoint = true;

			}

			if(objCommand.strCommand.equals("right")) {

				Variable objVariable =
						objCommand.objExpression.evaluate(
									this.objVariables);

				intDegree = intDegree + objVariable.intValue;

			}

			if(objCommand.strCommand.equals("left")) {

				Variable objVariable =
						objCommand.objExpression.evaluate(
									this.objVariables);

				intDegree = intDegree - objVariable.intValue;

			}

			Point objPoint = new Point();

			objPoint.intX =
					objCurrentPos.intX
					+ Utils.getInteger(
							intDistance.doubleValue() * Math.cos(
									Utils.getRadian(
											intDegree.doubleValue()
									)
					));

			objPoint.intY =
					objCurrentPos.intY
					+ Utils.getInteger(
							intDistance.doubleValue() * Math.sin(
									Utils.getRadian(
											intDegree.doubleValue()
									)
					));

			if (blnAddPoint == true) {

				this.objCurrentPos = objPoint;

				objPoints.add(objPoint);

			}

			if(objCommand.strCommand.equals("loop")) {

				Variable objVariable =
						objCommand.objExpression.evaluate(
									this.objVariables);

				for (Integer intIndex = 0; intIndex < objVariable.intValue; intIndex++) {
					objPoints.addAll(execute(((Control)objCommand).objCommandList));
				}

				this.objCurrentPos = objPoints.get(objPoints.size() - 1);

			}

		}

		return objPoints;

	}

	/***
	 * TestCase 20120405.001
	 * @return
	 * @throws Exception
	 */
	public Boolean testCase20120405001() throws Exception {

		String strProgram = new String();
		JLogo objLogo = new JLogo();
		List<Variable> objVariables = new ArrayList<Variable>();
		Variable objVariable = null;
		List<String> objSentences = new ArrayList<String>();

		strProgram = new String();
		strProgram += "set intDegree = 0;";

		objLogo = new JLogo();

		try {

			objSentences = objLogo.validate(strProgram);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		try {

			objLogo.objCommands =
					objLogo.parse(objSentences);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		Command objCommand = null;

		if (objLogo.objCommands.size() != 1) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objLogo.objCommands.get(0);
		objVariable = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable.strTerm.equals("intDegree") == false ||
				objVariable.intValue != 0) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		return true;

	}

	/***
	 * TestCase 20120412.003
	 * @return
	 * @throws TestFailureException
	 * @throws UnDeclaredVariableException
	 */
	public Boolean testCase20120412003()
			throws TestFailureException, UnDeclaredVariableException {

		String strProgram = new String();
		JLogo objLogo = new JLogo();
		List<Variable> objVariables = new ArrayList<Variable>();
		List<String> objSentences = new ArrayList<String>();
		Variable objVariable1 = null;
		Variable objVariable2 = null;

		strProgram = new String();
		strProgram += "set intDegree = 0;";
		strProgram += "loop 2;";
		strProgram += "  forward 10;";
		strProgram += "  right intDegree;";
		strProgram += "  set intDegree = intDegree + 10;";
		strProgram += "end;";

		objLogo = new JLogo();

		try {

			objSentences = objLogo.validate(strProgram);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		try {

			objLogo.objCommands =
					objLogo.parse(objSentences);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		Command objCommand = null;
		Control objControl = null;

		if (objLogo.objCommands.size() != 2) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objLogo.objCommands.get(0);
		objVariable1 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable1.strTerm.equals("intDegree") == false ||
				objVariable1.intValue != 0) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable1);

		objControl = (Control)objLogo.objCommands.get(1);
		objVariable2 = objControl.objExpression.evaluate(objVariables);

		if (objControl.strCommand.equals("loop") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 2) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objControl.objCommandList.get(0);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("forward") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 10) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objControl.objCommandList.get(1);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("right") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 0) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objControl.objCommandList.get(2);
		objVariable1 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable1.strTerm.equals("intDegree") == false ||
				objVariable1.intValue != 10) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		return true;
	}

	/***
	 * TestCase 20120412.001
	 * @return
	 * @throws Exception
	 */
	public Boolean testCase20120412001()
			throws TestFailureException, UnDeclaredVariableException {

		String strProgram = new String();
		JLogo objLogo = new JLogo();
		List<Variable> objVariables = new ArrayList<Variable>();
		Variable objVariable1 = null;
		Variable objVariable2 = null;
		Variable objVariable3 = null;
		Variable objVariable4 = null;
		Variable objVariable5 = null;
		List<String> objSentences = new ArrayList<String>();

		strProgram = new String();
		strProgram += "set intDegree1 = 1;";
		strProgram += "set intDegree2 = 2;";
		strProgram += "set intDegree3 = 3;";
		strProgram += "set intDegree4 = 4;";
		strProgram += "set intDegree5 = 5;";

		objLogo = new JLogo();

		try {

			objSentences = objLogo.validate(strProgram);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		try {

			objLogo.objCommands =
					objLogo.parse(objSentences);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		Command objCommand = null;

		if (objLogo.objCommands.size() != 5) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objLogo.objCommands.get(0);
		objVariable1 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable1.strTerm.equals("intDegree1") == false ||
				objVariable1.intValue != 1) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable1);

		objCommand = objLogo.objCommands.get(1);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable2.strTerm.equals("intDegree2") == false ||
				objVariable2.intValue != 2) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable2);

		objCommand = objLogo.objCommands.get(2);
		objVariable3 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable3.strTerm.equals("intDegree3") == false ||
				objVariable3.intValue != 3) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable3);

		objCommand = objLogo.objCommands.get(3);
		objVariable4 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable4.strTerm.equals("intDegree4") == false ||
				objVariable4.intValue != 4) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable4);

		objCommand = objLogo.objCommands.get(4);
		objVariable5 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable5.strTerm.equals("intDegree5") == false ||
				objVariable5.intValue != 5) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable5);

		return true;

	}

	/***
	 * TestCase 20120412.002
	 * @return
	 * @throws TestFailureException
	 * @throws UnDeclaredVariableException
	 */
	public Boolean testCase20120412002()
			throws TestFailureException, UnDeclaredVariableException {

		String strProgram = new String();
		JLogo objLogo = new JLogo();
		List<String> objSentences = new ArrayList<String>();
		List<Variable> objVariables = new ArrayList<Variable>();
		Variable objVariable1 = null;
		Variable objVariable2 = null;

		strProgram = new String();
		strProgram += "set intDegree = 10;";
		strProgram += "loop 2;";
		strProgram += "  forward 10;";
		strProgram += "  right intDegree;";
		strProgram += "end;";
		strProgram += "forward 12;";

		try {

			objSentences = objLogo.validate(strProgram);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		try {

			objLogo.objCommands = objLogo.parse(objSentences);

		} catch (Exception objExp) {
			System.err.println("プログラムの構文チェックで例外が発生しました。");
			throw new TestFailureException();
		}

		Command objCommand = null;
		Control objControl = null;

		if (objLogo.objCommands.size() != 3) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objLogo.objCommands.get(0);
		objVariable1 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("set") == false ||
				objVariable1.strTerm.equals("intDegree") == false ||
				objVariable1.intValue != 10) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable1);

		objControl = (Control)objLogo.objCommands.get(1);
		objVariable2 = objControl.objExpression.evaluate(objVariables);

		if (objControl.strCommand.equals("loop") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 2) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objControl.objCommandList.get(0);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("forward") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 10) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objControl.objCommandList.get(1);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("right") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 10) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objCommand = objLogo.objCommands.get(2);
		objVariable2 = objCommand.objExpression.evaluate(objVariables);

		if (objCommand.strCommand.equals("forward") == false ||
				objVariable2.strTerm != null ||
				objVariable2.intValue != 12) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		return true;

	}

}
