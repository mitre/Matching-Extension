/**
 *
 */
package org.mitre.base;

import java.util.List;

import org.nlogo.api.ClassManager;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.ExtensionManager;
import org.nlogo.api.ImportErrorHandler;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.core.ExtensionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author srohrer
 *
 */
@SpringBootApplication
public class MarketSimulator implements ClassManager {

  /**
   * for netlogo plugin
   */
  private static final long serialVersionUID = -8256754655837394131L;

  // for running the market simulator
  @Autowired
  private final OrderBook ob = new OrderBook();

  @Autowired
  private final MatchingEngine me = new MatchingEngine(ob);


  /**
   *
   * @param workspace
   */
  public MarketSimulator() {
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(MarketSimulator.class, args);
  }

  @Override
  public List<String> additionalJars() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void clearAll() {
    // TODO Auto-generated method stub

  }

  @Override
  public StringBuilder exportWorld() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void importWorld(List<String[]> lines, ExtensionManager reader, ImportErrorHandler handler)
      throws ExtensionException {
    // TODO Auto-generated method stub

  }

  @Override
  public void load(PrimitiveManager primManager) throws ExtensionException {
    // TODO Auto-generated method stub

  }

  @Override
  public ExtensionObject readExtensionObject(ExtensionManager reader, String typeName, String value)
      throws ExtensionException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void runOnce(ExtensionManager em) throws ExtensionException {
    // TODO Auto-generated method stub

  }

  @Override
  public void unload(ExtensionManager em) throws ExtensionException {
    // TODO Auto-generated method stub

  }

}
