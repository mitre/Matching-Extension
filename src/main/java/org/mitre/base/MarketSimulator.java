/**
 *
 */
package org.mitre.base;

import javax.swing.JPanel;

import org.nlogo.api.NetLogoListener;
import org.nlogo.core.CompilerException;
import org.nlogo.window.GUIWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author srohrer
 *
 */
@SpringBootApplication
public class MarketSimulator extends JPanel implements NetLogoListener {

	/**
	 * for netlogo plugin
	 */
	private static final long serialVersionUID = -8256754655837394131L;
	private transient GUIWorkspace wspace;


	// for running the market simulator
	@Autowired
	private final transient OrderBook ob = new OrderBook();

	@Autowired
	private final transient MatchingEngine me = new MatchingEngine(ob);


	/**
	 *
	 * @param workspace
	 */
	public MarketSimulator(GUIWorkspace workspace) {
		this.wspace = workspace;
		wspace.listenerManager.addListener(this);
	}

	/**
	 *
	 */
	public MarketSimulator() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void buttonPressed(String buttonName) {
		// TODO Auto-generated method stub

	}


	@Override
	public void buttonStopped(String buttonName) {
		// TODO Auto-generated method stub

	}


	@Override
	public void chooserChanged(String name, Object value, boolean valueChanged) {
		// TODO Auto-generated method stub

	}


	@Override
	public void codeTabCompiled(String text, CompilerException errorMsg) {
		// TODO Auto-generated method stub

	}


	@Override
	public void commandEntered(String owner, String text, char agentType, CompilerException errorMsg) {
		// TODO Auto-generated method stub

	}


	@Override
	public void inputBoxChanged(String name, Object value, boolean valueChanged) {
		// TODO Auto-generated method stub

	}


	@Override
	public void modelOpened(String name) {
		// TODO Auto-generated method stub

	}


	@Override
	public void possibleViewUpdate() {
		// TODO Auto-generated method stub

	}


	@Override
	public void sliderChanged(String name, double value, double min, double increment, double max, boolean valueChanged,
			boolean buttonReleased) {
		// TODO Auto-generated method stub

	}


	@Override
	public void switchChanged(String name, boolean value, boolean valueChanged) {
		// TODO Auto-generated method stub

	}


	@Override
	public void tickCounterChanged(double ticks) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MarketSimulator.class, args);
	}

}
