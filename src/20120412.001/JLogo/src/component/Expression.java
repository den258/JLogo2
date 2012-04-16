package component;

import java.util.ArrayList;
import java.util.List;

import constants.Constant;
import exceptions.NotHaveEqualExpressionException;
import exceptions.TestFailureException;
import exceptions.UnDeclaredVariableException;

import utils.Utils;

public class Expression implements Cloneable {

	public List<Term> objExpression = new ArrayList<Term>();
	public String strTargetVariableLabel = new String();

//	@Override
//	protected Object clone()
//			throws CloneNotSupportedException {
//
//		Expression objExpression;
//
//		try {
//			objExpression = new Expression("");
//		} catch (NotHaveEqualExpressionException e) {
//			throw new CloneNotSupportedException();
//		}
//
//		objExpression.objExpression = ((Expression)super.clone()).objExpression;
//		objExpression.strTargetVariableLabel = ((Expression)super.clone()).strTargetVariableLabel;
//
//		return objExpression;
//
//	}

	public Expression(
			String strArgExpression)
					throws NotHaveEqualExpressionException {

		this.strTargetVariableLabel = getLeftOfEqual(strArgExpression);

		for (String strTerm : getRightOfEqual(
				strArgExpression).split(" ")) {
			if (Utils.isNullOrEmptyString(strTerm) == false) {
				this.objExpression.add(new Term(strTerm));
			}
		}

	}

	public Variable evaluate(List<Variable> objArgVariables)
			throws UnDeclaredVariableException {

		return new Variable(
				this.strTargetVariableLabel,
				execute(objArgVariables));

	}

	public Integer execute(
			String strArgNumber1,String strArgOperator,String strArgNumber2) {

		Integer intNumber1 = Utils.getNumeric(strArgNumber1);
		Integer intNumber2 = Utils.getNumeric(strArgNumber2);

		if (strArgOperator.equals("+") == true) {
            return intNumber1 + intNumber2;
		}

		if (strArgOperator.equals("-") == true) {
            return intNumber1 - intNumber2;
		}

		if (strArgOperator.equals("*") == true) {
            return intNumber1 * intNumber2;
		}

		if (strArgOperator.equals("/") == true) {
            return intNumber1 / intNumber2;
		}

		return 0;

    }

    public Integer execute(
    		List<Variable> objArgVariables)
    				throws UnDeclaredVariableException {

		List<Integer> objExecSeqList = new ArrayList<Integer>();

		List<Term> objExpression = Utils.getCopyOfExpression(this.objExpression);

		for (Term objTerm : objExpression) {

			if (objTerm.getType().equals(Constant.STR_TERM_TYPE_VARIABLE)) {

				Variable objVariable =
						Utils.getVariable(
								objArgVariables, objTerm.strTerm);

				objTerm.strTerm = objVariable.intValue.toString();

			}

		}

		for (Integer intIndex = 0; intIndex < objExpression.size(); intIndex++){
			if (objExpression.get(intIndex).getType().equals(Constant.STR_TERM_TYPE_OPERATOR)) {
				if (objExpression.get(intIndex).strTerm.equals("*")
						|| objExpression.get(intIndex).strTerm.equals("/")) {
					objExecSeqList.add(intIndex);
				}
			}
		}

		for (Integer intIndex = 0; intIndex < objExpression.size(); intIndex++) {
			if (objExpression.get(intIndex).getType().equals(Constant.STR_TERM_TYPE_OPERATOR)) {
				if (objExpression.get(intIndex).strTerm.equals("+")
						|| objExpression.get(intIndex).strTerm.equals("-")) {
					objExecSeqList.add(intIndex);
				}
			}
		}

		for (int intIndex0 = 0; intIndex0 < objExecSeqList.size(); intIndex0++ ) {

			String strNumber =
					execute(
							objExpression.get(objExecSeqList.get(intIndex0) - 1).strTerm,
							objExpression.get(objExecSeqList.get(intIndex0)).strTerm,
							objExpression.get(objExecSeqList.get(intIndex0) + 1).strTerm).toString();

			objExpression.remove(objExecSeqList.get(intIndex0) - 1);
			objExpression.remove(objExecSeqList.get(intIndex0) - 1);
			objExpression.remove(objExecSeqList.get(intIndex0) - 1);

			objExpression.add(objExecSeqList.get(intIndex0) - 1, new Term(strNumber));

			for (int intIndex1 = 0; intIndex1 < objExecSeqList.size(); intIndex1++) {

				if (objExecSeqList.get(intIndex1) > objExecSeqList.get(intIndex0)) {
					objExecSeqList.set(intIndex1, objExecSeqList.get(intIndex1) - 2);
				}

			}

		}

		return Utils.getNumeric(objExpression.get(0).strTerm);

	}

	private String getRightOfEqual(
			String strArgExpression)
					throws NotHaveEqualExpressionException {

		Integer intEqualPosition = strArgExpression.indexOf("=");

		if (intEqualPosition == -1) {
			return strArgExpression;
		} else {
			return strArgExpression.substring(intEqualPosition + 1);
		}

	}

	private String getLeftOfEqual(
			String strArgExpression)
					throws NotHaveEqualExpressionException {

		Integer intEqualPosition = strArgExpression.indexOf("=");

		if (intEqualPosition == -1) {
			return null;
		} else {
			return strArgExpression.substring(0, intEqualPosition - 1);
		}

	}

	/***
	 * TestCase 20120411.001
	 * @return
	 * @throws TestFailureException
	 * @throws Exception
	 */
	public Boolean testCase20120411001() throws TestFailureException {

		Expression objExpression = null;
		List<Variable> objVariables = new ArrayList<Variable>();
		Variable objVariable1 = null;
		Variable objVariable2 = null;
		Variable objVariable3 = null;
		Variable objVariable4 = null;
		Variable objVariable5 = null;
		Variable objVariable6 = null;

		try {
			objExpression = new Expression("intVar1 = 10 * 2");
			objVariable1 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable1);

		if (objVariable1.strTerm.equals("intVar1") == false ||
				objVariable1.intValue != 20) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar2 = 10 + 2");
			objVariable2 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable2);

		if (objVariable2.strTerm.equals("intVar2") == false ||
				objVariable2.intValue != 12) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar3 = 10 - 2");
			objVariable3 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable3);

		if (objVariable3.strTerm.equals("intVar3") == false ||
				objVariable3.intValue != 8) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar4 = 10 / 2");
			objVariable4 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable4);

		if (objVariable4.strTerm.equals("intVar4") == false ||
				objVariable4.intValue != 5) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar5 = 2 - 10");
			objVariable5 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		objVariables.add(objVariable5);

		if (objVariable5.strTerm.equals("intVar5") == false ||
				objVariable5.intValue != -8) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar6 = intVar1 - 2");
			objVariable6 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		if (objVariable6.strTerm.equals("intVar6") == false ||
				objVariable6.intValue != 18) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		try {
			objExpression = new Expression("intVar6 = 2 - intVar1");
			objVariable6 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		if (objVariable6.strTerm.equals("intVar6") == false ||
				objVariable6.intValue != -18) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		return true;

	}

	/***
	 * TestCase 20120412.001
	 * @return
	 * @throws TestFailureException
	 * @throws Exception
	 */
	public Boolean testCase20120412001() throws TestFailureException {

		Expression objExpression = null;
		List<Variable> objVariables = new ArrayList<Variable>();
		Variable objVariable1 = null;

		try {
			objExpression = new Expression("28");
			objVariable1 = objExpression.evaluate(objVariables);
		} catch (Exception e) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		if (objVariable1.strTerm != null ||
				objVariable1.intValue != 28) {
			System.err.println("プログラムに誤りがあります。");
			throw new TestFailureException();
		}

		return true;

	}

}
