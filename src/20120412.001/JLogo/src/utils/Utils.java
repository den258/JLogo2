package utils;

import java.util.ArrayList;
import java.util.List;

import component.Term;
import component.Variable;

import constants.Constant;
import exceptions.UnDeclaredVariableException;

public class Utils {

	public static Boolean isNumeric(String strValue) {

		try {
			Integer.parseInt(strValue);
		} catch (Exception objExp) {
			return false;
		}
		return true;

	}

	public static Boolean isOperator(String strArgTerm) {

		if (strArgTerm.equals("+") ||
				strArgTerm.equals("-") ||
				strArgTerm.equals("*") ||
				strArgTerm.equals("/") ) {
			return true;
		} else {
			return false;
		}

	}

	public static Boolean isVariable(String strArgTerm) {

		if(isNumeric(strArgTerm)) {
			return false;
		}

		if (isOperator(strArgTerm)) {
			return false;
		}

		return true;

	}

	public static Integer getNumeric(String strValue) {

		if (isNumeric(strValue) == true) {
			return Integer.parseInt(strValue);
		} else {
			return 0;
		}

	}

	public static Double getRadian(Double dblDegree) {
		return dblDegree * (Math.PI / 180);
	}

	public static String getString(Integer intValue) {
		return intValue.toString();
	}

	public static Integer getInteger(Double dblValue) {
		return dblValue.intValue();
	}

	public static String getTermType(String strArgTerm) {

    	if (Utils.isNumeric(strArgTerm))
        {
            return Constant.STR_TERM_TYPE_NUMBER;
        }

        if (Utils.isOperator(strArgTerm))
        {
            return Constant.STR_TERM_TYPE_OPERATOR;
        }

        if (Utils.isVariable(strArgTerm))
        {
            return Constant.STR_TERM_TYPE_VARIABLE;
        }

        return Constant.STR_TERM_TYPE_ERROR;

    }

	public static Variable getVariable(
			List<Variable> objVariables, String strTerm)
					throws UnDeclaredVariableException {

		for (Variable objVariable : objVariables) {
			if (objVariable.strTerm.equals(strTerm)) {
				return objVariable;
			}
		}

		return null;

	}

	public static Boolean isNullOrEmptyString(String strArg) {

		if (strArg == null) {
			return true;
		}

		if (strArg.equals("")) {
			return true;
		}

		return false;

	}

	public static List<Term> getCopyOfExpression(List<Term> objArgExpression) {

		List<Term> objExpression = new ArrayList<Term>();

		for (Term objTerm : objArgExpression) {
			objExpression.add(new Term(objTerm.strTerm));
		}

		return objExpression;

	}
}
