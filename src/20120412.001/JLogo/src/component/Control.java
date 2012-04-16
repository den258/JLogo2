package component;

import java.util.ArrayList;
import java.util.List;

public class Control extends Command {

	public List<Command> objCommandList = new ArrayList<Command>();

	public Control(String strArgCommand, Expression objArgExpression) {
		super(strArgCommand, objArgExpression);
	}

}
